package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.javafx.collections.NonIterableChange;
import io.github.thomashan.tradingchart.ui.data.OhlcData;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChartConstants.CHART;
import static io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChartConstants.DATA;
import static io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChartConstants.NODE;
import static io.github.thomashan.tradingchart.javafx.scene.chart.SeriesConstants.NAME;

public class Series<O extends OhlcData<O, ?>> {

    // -------------- PRIVATE PROPERTIES ----------------------------------------

    /**
     * the style class for default color for this series
     */
    String defaultColorStyleClass;
    boolean setToRemove = false;

    private List<OhlcChart.Data<O>> displayedData = new ArrayList<>();

    public List<OhlcChart.Data<O>> getDisplayedData() {
        return displayedData;
    }

    private final ListChangeListener<OhlcChart.Data<O>> dataChangeListener = changedData -> {
        ObservableList<? extends OhlcChart.Data<O>> data = changedData.getList();
        final OhlcChart<O> chart = getChart();
        while (changedData.next()) {
            if (chart != null) {
                // RT-25187 Probably a sort happened, just reorder the pointers and return.
                if (changedData.wasPermutated()) {
                    displayedData.sort((o1, o2) -> data.indexOf(o2) - data.indexOf(o1));
                    return;
                }

                Set<OhlcChart.Data<O>> dupCheck = new HashSet<>(displayedData);
                dupCheck.removeAll(changedData.getRemoved());
                for (OhlcChart.Data<O> d : changedData.getAddedSubList()) {
                    if (!dupCheck.add(d)) {
                        throw new IllegalArgumentException("Duplicate data added");
                    }
                }

                // update data items reference to series
                for (OhlcChart.Data<O> item : changedData.getRemoved()) {
                    item.setToRemove(true);
                }

                if (changedData.getAddedSize() > 0) {
                    for (OhlcChart.Data<O> itemPtr : changedData.getAddedSubList()) {
                        if (itemPtr.getToRemove()) {
                            if (chart != null) {
                                chart.dataBeingRemovedIsAdded(itemPtr, Series.this);
                            }
                            itemPtr.setToRemove(false);
                        }
                    }

                    for (OhlcChart.Data<O> d : changedData.getAddedSubList()) {
                        d.setSeries(Series.this);
                    }
                    if (changedData.getFrom() == 0) {
                        displayedData.addAll(0, changedData.getAddedSubList());
                    } else {
                        displayedData.addAll(displayedData.indexOf(data.get(changedData.getFrom() - 1)) + 1, changedData.getAddedSubList());
                    }
                }
                // inform chart
                chart.dataItemsChanged(Series.this, changedData.getRemoved(), changedData.getFrom(), changedData.getTo(), changedData.wasPermutated());
            } else {
                Set<OhlcChart.Data<O>> dupCheck = new HashSet<>();
                for (OhlcChart.Data<O> d : data) {
                    if (!dupCheck.add(d)) {
                        throw new IllegalArgumentException("Duplicate data added");
                    }
                }

                for (OhlcChart.Data<O> d : changedData.getAddedSubList()) {
                    d.setSeries(Series.this);
                }

            }
        }
    };

    // -------------- PUBLIC PROPERTIES ----------------------------------------

    /**
     * Reference to the chart this series belongs to
     */
    private final ReadOnlyObjectWrapper<OhlcChart<O>> chart = new ReadOnlyObjectWrapper<>(this, CHART) {
        @Override
        protected void invalidated() {
            if (get() == null) {
                displayedData.clear();
            } else {
                displayedData.addAll(getData());
            }
        }
    };

    public OhlcChart<O> getChart() {
        return chart.get();
    }

    protected void setChart(OhlcChart<O> value) {
        chart.set(value);
    }

    public ReadOnlyObjectProperty<OhlcChart<O>> chartProperty() {
        return chart.getReadOnlyProperty();
    }

    /**
     * The user displayable name for this series
     */
    private final StringProperty name = new StringPropertyBase() {
        @Override
        protected void invalidated() {
            get(); // make non-lazy
            if (getChart() != null) {
                getChart().seriesNameChanged();
            }
        }

        @Override
        public Object getBean() {
            return Series.this;
        }

        @Override
        public String getName() {
            return NAME;
        }
    };

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }

    /**
     * The node to display for this series. This is created by the chart if it uses nodes to represent the whole
     * series. For example line chart uses this for the line but scatter chart does not use it. This node will be
     * set as soon as the series is added to the chart. You can then get it to add mouse listeners etc.
     */
    private ObjectProperty<Node> node = new SimpleObjectProperty<>(this, NODE);

    public final Node getNode() {
        return node.get();
    }

    public final void setNode(Node value) {
        node.set(value);
    }

    public final ObjectProperty<Node> nodeProperty() {
        return node;
    }

    /**
     * ObservableList of data items that make up this series
     */
    private final ObjectProperty<ObservableList<OhlcChart.Data<O>>> data = new ObjectPropertyBase<>() {
        private ObservableList<OhlcChart.Data<O>> old;

        @Override
        protected void invalidated() {
            final ObservableList<OhlcChart.Data<O>> current = getValue();
            // add remove listeners
            if (old != null) {
                old.removeListener(dataChangeListener);
            }
            if (current != null) {
                current.addListener(dataChangeListener);
            }
            // fire data change event if series are added or removed
            if (old != null || current != null) {
                final List<OhlcChart.Data<O>> removed = (old != null) ? old : Collections.emptyList();
                final int toIndex = current != null ? current.size() : 0;
                // let data listener know all old data have been removed and new data that has been added
                if (toIndex > 0 || !removed.isEmpty()) {
                    dataChangeListener.onChanged(new NonIterableChange<>(0, toIndex, current) {
                        @Override
                        public List<OhlcChart.Data<O>> getRemoved() {
                            return removed;
                        }

                        @Override
                        protected int[] getPermutation() {
                            return new int[0];
                        }
                    });
                }
            } else if (old != null && old.size() > 0) {
                // let series listener know all old series have been removed
                dataChangeListener.onChanged(new NonIterableChange<>(0, 0, current) {
                    @Override
                    public List<OhlcChart.Data<O>> getRemoved() {
                        return old;
                    }

                    @Override
                    protected int[] getPermutation() {
                        return new int[0];
                    }
                });
            }
            old = current;
        }

        @Override
        public Object getBean() {
            return Series.this;
        }

        @Override
        public String getName() {
            return DATA;
        }
    };

    public ObservableList<OhlcChart.Data<O>> getData() {
        return data.getValue();
    }

    public void setData(ObservableList<OhlcChart.Data<O>> value) {
        data.setValue(value);
    }

    public ObjectProperty<ObservableList<OhlcChart.Data<O>>> dataProperty() {
        return data;
    }

    // -------------- CONSTRUCTORS ----------------------------------------------

    /**
     * Construct a empty series
     */
    public Series() {
        this(FXCollections.observableArrayList());
    }

    /**
     * Constructs a Series and populates it with the given {@link ObservableList} data.
     *
     * @param data ObservableList of Data
     */
    public Series(ObservableList<OhlcChart.Data<O>> data) {
        setData(data);
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setSeries(this);
        }
    }

    /**
     * Constructs a named Series and populates it with the given {@link ObservableList} data.
     *
     * @param name a name for the series
     * @param data ObservableList of Data
     */
    public Series(String name, ObservableList<OhlcChart.Data<O>> data) {
        this(data);
        setName(name);
    }

    // -------------- PUBLIC METHODS ----------------------------------------------

    /**
     * Returns a string representation of this {@code Series} object.
     *
     * @return a string representation of this {@code Series} object.
     */
    @Override
    public String toString() {
        return "Series[" + getName() + "]";
    }

    // -------------- PRIVATE/PROTECTED METHODS -----------------------------------

    /*
     * The following methods are for manipulating the pointers in the linked list
     * when data is deleted.
     */
    private void removeDataItemRef(OhlcChart.Data<O> item) {
        if (item != null) {
            item.setToRemove(false);
        }
        displayedData.remove(item);
    }

    int getItemIndex(OhlcChart.Data<O> item) {
        return displayedData.indexOf(item);
    }

    OhlcChart.Data<O> getItem(int i) {
        return displayedData.get(i);
    }

    int getDataSize() {
        return displayedData.size();
    }
}
