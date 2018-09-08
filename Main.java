package src.sample;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main extends Application {

    public Main() throws SQLException {
    }

    static class Data {
        private final SimpleStringProperty id;
        private final SimpleStringProperty date;
        private final SimpleStringProperty latitude;


        private final SimpleStringProperty depth;

        private final SimpleStringProperty longitude;

        public String getId() {
            return id.get();
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public String getDate() {
            return date.get();
        }


        public SimpleStringProperty dateProperty() {
            return date;
        }

        public void setDate(String date) {
            this.date.set(date);
        }

        public String getLatitude() {
            return latitude.get();
        }

        public LocalDateTime getTime() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

            return LocalDateTime.parse(this.getDate(), formatter);
        }

        public SimpleStringProperty latitudeProperty() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude.set(latitude);
        }

        public String getDepth() {
            return depth.get();
        }

        public SimpleStringProperty depthProperty() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth.set(depth);
        }

        public String getLongitude() {
            return longitude.get();
        }

        public SimpleStringProperty longitudeProperty() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude.set(longitude);
        }

        public String getMaginitude() {
            return maginitude.get();
        }

        public SimpleStringProperty maginitudeProperty() {
            return maginitude;
        }

        public void setMaginitude(String maginitude) {
            this.maginitude.set(maginitude);
        }

        public String getRegion() {
            return region.get();
        }

        public SimpleStringProperty regionProperty() {
            return region;
        }

        public void setRegion(String region) {
            this.region.set(region);
        }

        private final SimpleStringProperty maginitude;

        private final SimpleStringProperty region;

        public Data(String a, String b, String c, String d, String e, String f, String g) {
            this.id = new SimpleStringProperty(a);
            this.date = new SimpleStringProperty(b);
            this.latitude = new SimpleStringProperty(c);
            this.depth = new SimpleStringProperty(e);
            this.longitude = new SimpleStringProperty(d);
            this.maginitude = new SimpleStringProperty(f);
            this.region = new SimpleStringProperty(g);

        }

        public String getDATE() {
            return this.getDate().substring(0, 10);
        }
    }

    static Properties pps;
    final Connection con = DriverManager.getConnection("jdbc:sqlite:" + pps.getProperty("sql_address"));
    final ObservableList<Data> data = FXCollections.observableArrayList();


    public void start(Stage stage) throws IOException, SQLException {

        TableView table = new TableView();


//connecting to the database
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            System.err.println("Cannot find the driver.");
            System.exit(1);
        }

        con.setAutoCommit(false);//the commit of sql command is in the charge of application
        // System.err.println("Successfully connected to the database.");


        PreparedStatement stmt;
        ResultSet rs;
        //read information from database
        stmt = con.prepareStatement("select id,UTC_date,latitude,longitude,depth,magnitude,region from quakes");
        rs = stmt.executeQuery();
        while (rs.next()) {
            //add the information to the linked list of data by the way of constructing function
            data.add(new Data(rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
        }

        // set the name of column in the table and various widths
        TableColumn<Data, String> idCol = new TableColumn("id");
        TableColumn<Data, String> dateCol = new TableColumn("date");
        TableColumn<Data, String> latitudeCol = new TableColumn("latitude");

        TableColumn<Data, String> longitudeCol = new TableColumn("longitude");
        TableColumn<Data, String> depthCol = new TableColumn("depth");
        TableColumn<Data, String> maginitudeCol = new TableColumn("magnitude");
        TableColumn<Data, String> regionCol = new TableColumn("region");
        idCol.setMaxWidth(200);
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        latitudeCol.setCellValueFactory(cellData -> cellData.getValue().latitudeProperty());
        latitudeCol.setMaxWidth(100);
        dateCol.setMinWidth(200);
        longitudeCol.setCellValueFactory(cellData -> cellData.getValue().longitudeProperty());
        longitudeCol.setMaxWidth(100);
        depthCol.setCellValueFactory(cellData -> cellData.getValue().depthProperty());
        depthCol.setMaxWidth(100);
        maginitudeCol.setMaxWidth(100);
        maginitudeCol.setCellValueFactory(cd -> cd.getValue().maginitudeProperty());
        regionCol.setCellValueFactory(c -> c.getValue().regionProperty());
        regionCol.setMinWidth(300);
        //put all these columns to the table
        table.getColumns().addAll(idCol, dateCol, latitudeCol, longitudeCol, depthCol, maginitudeCol, regionCol);


        final DatePicker datePicker = new DatePicker(LocalDate.now());
        final DatePicker datePicker1 = new DatePicker(LocalDate.now());
        //print the selecting time
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            System.out.println("Selected date: " + date);
        });
        TimeSpinner spinner = new TimeSpinner(LocalTime.now());
        TimeSpinner spinner1 = new TimeSpinner(LocalTime.now());


        GridPane root = new GridPane();
        root.setVgap(5);

        Label label = new Label("start");
        label.setTextFill(Color.web("#00FA9A"));
        root.add(label, 0, 0);
        root.add(datePicker, 0, 1);
        root.add(spinner, 1, 1);
        Label label2 = new Label("end");
        label2.setTextFill(Color.web("#00FA9A"));
        root.add(label2, 0, 2);
        root.add(datePicker1, 0, 3);
        root.add(spinner1, 1, 3);
        Label label3 = new Label("magnitude:    5.0");
        label3.setTextFill(Color.web("#00FA9A"));
        Button button = new Button("search");

        root.add(label3, 0, 4);
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(10);
        slider.setValue(5);
        slider.setShowTickLabels(true);

        slider.setShowTickMarks(true);
        slider.setBlockIncrement(0.1);
        slider.setStyle("-fx-control-inner-background: palegreen;");


        root.add(slider, 0, 5);
        root.add(button, 2, 7);
        Button update = new Button();
        update.setText("update");


        root.add(update, 2, 6);


        //read the address of the picture
        String path = pps.getProperty("picture_address");
        Image image1 = new Image(new FileInputStream(path));
        ImageView imageView = new ImageView(image1);
        imageView.setFitWidth(image1.getWidth());
        imageView.setFitHeight(image1.getHeight());
        WorldMap worldMap = new WorldMap(image1, WorldMap.Projection.MERCATOR, 180);
        double width = image1.getWidth();
        double height = image1.getHeight();


        //generating the components in the chart
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");
        final StackedBarChart<String, Number> sbc = new StackedBarChart<String, Number>(xAxis, yAxis);
        final XYChart.Series<String, Number> series1 =
                new XYChart.Series<>();
        final XYChart.Series<String, Number> series2 =
                new XYChart.Series<>();
        final XYChart.Series<String, Number> series3 =
                new XYChart.Series<>();
        final XYChart.Series<String, Number> series4 =
                new XYChart.Series<>();
        final XYChart.Series<String, Number> series5 =
                new XYChart.Series<>();
        series1.setName("Under 3.0");
        series2.setName("3.0 to 4.0");
        series3.setName("4.0 to 5.0");
        series4.setName("5.0 to 6.0");
        series5.setName("Above 6.0");


        table.setMinSize(500, 500);
        VBox vBox = new VBox();
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(imageView);
        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab();
        tab1.setText("Data Table");
        final TextField searchByRegion = new TextField();
        searchByRegion.setPromptText("All over the world");


        // renewing the value shown in the slider
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                label3.setText(String.format("magnitude:    " + "%.1f", slider.getValue()));

            }
        });
        FilteredList<Data> filteredData = new FilteredList<Data>(data, e -> true);
        Alert information = new Alert(Alert.AlertType.INFORMATION, "Successfully update the data from the website");


        // updating data
        update.setOnAction(

                (ActionEvent e) -> {

                    PreparedStatement stmt2 = null;
                    ResultSet rs2 = null;
                    try {
                        // selecting the last time from the database
                        stmt2 = con.prepareStatement("select max(UTC_date) from quakes");
                        rs2 = stmt2.executeQuery();
                        LocalDateTime localDateTime = null;
                        LocalDateTime latest = null;
                        if (rs2.next()) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                            latest = LocalDateTime.parse(rs2.getString(1), formatter);
                        }
                        stmt2.close();
                        rs2.close();
                        System.out.println(latest);


                        //scrawling data circularly
                        int j = 0;
                        while (scrawl(j, latest))
                            j++;
                        //showing the success of renewing
                        information.showAndWait();


                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }


                });
        /**
         *
         */

        //finding the information of earthquake meeting the condition
        button.setOnAction((ActionEvent e) ->
        {


            //clear the previous data in the chart
            series1.getData().clear();
            series2.getData().clear();
            series3.getData().clear();
            series4.getData().clear();
            series5.getData().clear();
            sbc.getData().clear();
            // read the start time and end time of selection
            LocalDateTime dateTime1 = LocalDateTime.of(datePicker.getValue(), spinner.getValue());
            LocalDateTime dateTime2 = LocalDateTime.of(datePicker1.getValue(), spinner1.getValue());
            //generating canvas to draw dot in the map
            final Canvas canvas = new Canvas(width, height);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.RED);

            TreeMap<String, TreeMap<Integer, Integer>> treeMap = new TreeMap<>();
            filteredData.setPredicate(h -> {
                //if the earthquake meeting condition，we show it in the table , draw the point on the map and record it in the chart
                if (Double.valueOf(h.getMaginitude()) <= slider.getValue() && h.getTime().isBefore(dateTime2) && h.getTime().isAfter(dateTime1)) {
                    h.getDate();
                    double latitude = Double.valueOf(h.getLatitude());
                    double longtitude = Double.valueOf(h.getLongitude());
                    int[] xy = worldMap.imgxy(latitude, longtitude);
                    //obtaining the location by latitude and longitude
                    //determining the size of circle by the strength of earthquake
                    double maginitude = Double.valueOf(h.getMaginitude());
                    int dimageter = (int) (5 * maginitude - 4) / 3;
                    gc.fillOval(xy[0] - dimageter / 2, xy[1] - dimageter / 2, dimageter, dimageter);


                    //initialization
                    if (treeMap.get(h.getDATE()) == null)
                        treeMap.put(h.getDATE(), new TreeMap<Integer, Integer>());

                    // count the times of earthquake in each period
                    if (Double.valueOf(h.getMaginitude()) < 3.0) {
                        Integer a = treeMap.get(h.getDATE()).get(1);
                        if (a == null)
                            treeMap.get(h.getDATE()).put(1, 1);
                        else
                            treeMap.get(h.getDATE()).put(1, a + 1);
                    } else if (Double.valueOf(h.getMaginitude()) < 4.0 && Double.valueOf(h.getMaginitude()) >= 3.0) {
                        Integer a = treeMap.get(h.getDATE()).get(2);
                        if (a == null)
                            treeMap.get(h.getDATE()).put(2, 1);
                        else
                            treeMap.get(h.getDATE()).put(2, a + 1);
                    } else if (Double.valueOf(h.getMaginitude()) < 5.0 && Double.valueOf(h.getMaginitude()) >= 4.0) {
                        Integer a = treeMap.get(h.getDATE()).get(3);
                        if (a == null)
                            treeMap.get(h.getDATE()).put(3, 1);
                        else
                            treeMap.get(h.getDATE()).put(3, a + 1);
                    } else if (Double.valueOf(h.getMaginitude()) < 6.0 && Double.valueOf(h.getMaginitude()) >= 5.0) {
                        Integer a = treeMap.get(h.getDATE()).get(4);
                        if (a == null)
                            treeMap.get(h.getDATE()).put(4, 1);
                        else
                            treeMap.get(h.getDATE()).put(4, a + 1);
                    } else {
                        Integer a = treeMap.get(h.getDATE()).get(5);
                        if (a == null)
                            treeMap.get(h.getDATE()).put(5, 1);
                        else
                            treeMap.get(h.getDATE()).put(5, a + 1);
                    }


                    return true;
                }
                return false;

            });
            ObservableList<Node> paneChildren;
            paneChildren = stackPane.getChildren();
            //delete the previous canvas
            if (paneChildren.size() > 1) {
                paneChildren.remove(1, paneChildren.size());
            }
            stackPane.getChildren().add(canvas);
            SortedList<Data> sortedData = new SortedList<Data>(filteredData);
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            //add selected and sorted data to table
            table.setItems(sortedData);


            //renew chart
            if (!treeMap.isEmpty()) {
                String date = treeMap.firstKey();
                while (date != null) {
                    TreeMap<Integer, Integer> treeMap2 = treeMap.get(date);
                    if (treeMap2.get(1) != null)
                        series1.getData().add(new XYChart.Data<>(date, treeMap2.get(1)));
                    if (treeMap2.get(2) != null)
                        series2.getData().add(new XYChart.Data<>(date, treeMap2.get(2)));
                    if (treeMap2.get(3) != null)
                        series3.getData().add(new XYChart.Data<>(date, treeMap2.get(3)));
                    if (treeMap2.get(4) != null)
                        series4.getData().add(new XYChart.Data<>(date, treeMap2.get(4)));
                    if (treeMap2.get(5) != null)
                        series5.getData().add(new XYChart.Data<>(date, treeMap2.get(5)));
                    date = treeMap.higherKey(date);
                }
                sbc.getData().addAll(series1, series2, series3, series4, series5);

            }


        });

        Label label6 = new Label();
        label6.setText(" Enter the region you want to look up");
        VBox vbox1 = new VBox();
        vbox1.getChildren().addAll(label6, searchByRegion, table);
        FilteredList<Data> filteredList = new FilteredList<Data>(filteredData, e -> true);
        searchByRegion.textProperty().addListener((observable, oldValue, newValue) -> {


            //inputting region，finding the information of earthquakes in the region
            filteredList.setPredicate(person -> {
                //if no input, showing all
                if (newValue == null || newValue.isEmpty())
                    return true;
                //only show the information of earthquakes in the input region
                if (person.getRegion().toLowerCase().contains(newValue.toLowerCase()))
                    return true;
                return false;
            });
            SortedList<Data> sortedData = new SortedList<Data>(filteredList);
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);
        });


        table.setOnMouseClicked((MouseEvent e) -> {
            // clicking one data set in the table and showing it on the map with a black circle, which is convenient for us to find the location
            if (table.getSelectionModel().getSelectedItem() != null) {
                Data selectedData = (Data) table.getSelectionModel().getSelectedItem();
                double latitude = Double.valueOf(selectedData.getLatitude());

                double longtitude = Double.valueOf(selectedData.getLongitude());
                int[] xy = worldMap.imgxy(latitude, longtitude);


                Canvas c = new Canvas(image1.getWidth(), image1.getHeight());
                GraphicsContext gcc = c.getGraphicsContext2D();
                gcc.setFill(Color.BLACK);

                double maginitude = Double.valueOf(selectedData.getMaginitude());
                int dimageter = (int) (5 * maginitude - 4) / 3;
                gcc.fillOval(xy[0] - dimageter / 2, xy[1] - dimageter / 2, dimageter, dimageter);
                gcc.strokeOval(xy[0] - 20, xy[1] - 20, 40, 40);
                ObservableList<Node> paneChild = stackPane.getChildren();

                if (paneChild.size() > 2) {
                    paneChild.remove(2, paneChild.size());
                }
                paneChild.add(c);


            }
        });

        tab1.setContent(vbox1);
        Tab tab2 = new Tab();
        tab2.setContent(stackPane);
        tab2.setText("Map");
        Tab tab3 = new Tab();
        tab3.setText("Chart");
        tab3.setContent(sbc);

        tabPane.getTabs().addAll(tab1, tab2, tab3);

        vBox.getChildren().addAll(root, tabPane);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(vBox);


        Scene scene = new Scene(hBox);
        scene.getStylesheets().add("Viper.css");
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.trustStore", "G:\\Java2\\11611519_project\\jssecacerts");//加载安全证书
        pps = new Properties();
        try {
            File file1 = new File("./Mercator.jpg");
            File file2 = new File("./earthquakes-1.sqlite");
            pps.put("picture_address",file1.getCanonicalPath());
            pps.put("sql_address",file2.getCanonicalPath());
            BufferedReader bf = new BufferedReader(new FileReader("project.cnf"));
            pps.load(bf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }

    /**
     * This is a function that scrawl the website to update the data  这是一个用来爬取最新地震信息的方法
     *
     * @param j      the page number of the website  这代表网页的页码
     * @param latest this is the latest date in our database, we want to update the newest date until we meet this one, then we stop updating
     *               这是数据库存在的最近的日期，我们想从网站中爬日期， 爬到这个日期为止
     * @return 当我们没有遇到数据库里的latest date的话，说明我们还没有爬完，需要翻页，return true会在上面的while循环中爬取下一页
     * 如果我们已经遇到了latest date 那么说明爬完了，我们不需要接着爬了，return false
     * @throws SQLException
     */
    boolean scrawl(int j, LocalDateTime latest) throws SQLException {


        Statement stmt = null;

        stmt = con.createStatement();


        try {
            Document doc = Jsoup
                    .connect("https://www.emsc-csem.org/Earthquake/?view=" + String.valueOf(j))
                    .get();

            //obtaining the data in the jth page of the earthquake web
            Elements table = doc.select("table");
            Elements row = table.get(3).select("tbody");

            for (Element b : row) {
                Elements rows = b.select("tr");
                for (int k = 1; k < rows.size(); k++) {
                    Element r = rows.get(k);
                    String[] arr = new String[9];
                    int i = 0;
                    if (r.attr("id").matches("[0-9]*"))
                        arr[i++] = r.attr("id");
                    //scrawl id
                    Elements cell = r.select("td");
                    boolean flag = true;
                    for (Element block : cell) {

                        //The first information scrawled-date
                        Elements time = block.select("b a");
                        if (flag && time.size() > 0) {
                            arr[i++] = time.get(0).text();
                            flag = false;
                        }
                        // other information to be scrawled
                        else if (!flag) {
                            if (block.text().trim().length() != 0) {
                                if (!block.toString().trim().contains("magtyp") && !block.toString().trim().contains("comment updatetimeno"))
                                    arr[i++] = block.text();
                                // removed two unrelated information
                            }
                        }
                        //quitting when we have scrawled the information needed
                        if (i == 9) break;
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

                    if (arr[1] != null) {

                        LocalDateTime now = LocalDateTime.parse(arr[1], formatter);
                        if (now.isAfter(latest)) {
                            //If the information is later than the latest information in the database, we need add it to the database.
                            //If the earthquake information including "S" or "W", we need to add "-" in front of them.
                            if (arr[3].equals("S")) arr[2] = "-" + arr[2];
                            if (arr[5].equals("W")) arr[4] = "-" + arr[4];

                            // preventing "," in the region
                            arr[8] = arr[8].replace("'", "\"");

                            // Only if there is no the earthquake in database,we can add it to the database
                            ResultSet rs = stmt.executeQuery("SELECT * from quakes where id =" + arr[0]);
                            if (rs.next()) {
                                System.out.println("replicate");
                            } else {
                                for (String a : arr) System.out.print(a + " ");

                                //adding it to the database and printing
                                stmt.executeUpdate("INSERT INTO quakes (id,UTC_date,latitude,longitude,depth,magnitude,region) VALUES"
                                        + "(" + arr[0] + ",'" + arr[1] + "'," + arr[2] + "," + arr[4] + "," + arr[6] + "," + arr[7] + ",'" + arr[8] + "');");

                                //creating the Data object and adding it to the observableArrayList, renewing the table
                                data.add(new Data(arr[0], arr[1], arr[2], arr[4], arr[6], arr[7], arr[8]));


                                con.commit();
                            }

                        } else {
                            return false;
                        }

                        System.out.println();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return true;
    }
}
