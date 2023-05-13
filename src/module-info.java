module mancala {
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.base;
	requires java.desktop;
	opens mancala to javafx.graphics;
}