/* doesn't work with source level 1.8:
module com.covid_fighters.gui {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.covid_fighters.gui to javafx.fxml;
    exports com.covid_fighters.gui;
}
*/
