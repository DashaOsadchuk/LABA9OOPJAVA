package sample;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Controller implements Initializable {

    private double startX, endX, stepX;
    private double maxY, minY;
    private Point[] points;
    StringBuilder infoTextBuild;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Canvas drawGraph;

    @FXML
    private TextField InputX;

    @FXML
    private TextField EndX;

    @FXML
    private TextField InputStep;

    @FXML
    private Button buttonGenerate;

    @FXML
    private Text ErrorText;

    @FXML
    private Text InfoText;

    @FXML @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert drawGraph != null : "fx:id=\"drawGraph\" was not injected: check your FXML file 'sample.fxml'.";
        assert InputX != null : "fx:id=\"InputX\" was not injected: check your FXML file 'sample.fxml'.";
        assert EndX != null : "fx:id=\"EndX\" was not injected: check your FXML file 'sample.fxml'.";
        assert InputStep != null : "fx:id=\"InputStep\" was not injected: check your FXML file 'sample.fxml'.";
        assert buttonGenerate != null : "fx:id=\"buttonGenerate\" was not injected: check your FXML file 'sample.fxml'.";
        assert ErrorText != null : "fx:id=\"ErrorText\" was not injected: check your FXML file 'sample.fxml'.";
        assert InfoText != null : "fx:id=\"InfoText\" was not injected: check your FXML file 'sample.fxml'.";
        buttonGenerate.setOnMouseClicked(event -> {
            ErrorText.setVisible(false);
            regenerateGraph();
        });
        InputX.setOnKeyReleased(event -> {
            ErrorText.setVisible(false);
            if (InputX.getText().length() > 0) {
                startX = Double.parseDouble(InputX.getText());
                System.out.println(InputX.getText());
            }
        });
        EndX.setOnKeyReleased(event -> {
            ErrorText.setVisible(false);
            if (EndX.getText().length() > 0) {
                endX = Double.parseDouble(EndX.getText());
                System.out.println(EndX.getText());
            }
        });
        InputStep.setOnKeyReleased(event -> {
            ErrorText.setVisible(false);
            if (InputStep.getText().length() > 0) {
                stepX = Double.parseDouble(InputStep.getText());
                System.out.println(InputStep.getText());
            }
        });
        InfoText.setVisible(false);
    }

    private void regenerateGraph() {
        if (stepX <= 0) {ErrorText.setText("Помилка! Крок замалий!"); ErrorText.setVisible(true); return;}
        if (startX >= endX) {ErrorText.setText("Помилка! Невірний проміжок!"); ErrorText.setVisible(true); return;}
        if (startX <= 0) {ErrorText.setText("Помилка! Проміжок повинен починатися\nз координати більше нуля!"); ErrorText.setVisible(true); return;}
        if (endX-startX < stepX) {ErrorText.setText("Помилка! Крок більше проміжку!"); ErrorText.setVisible(true); return;}
        run();
    }

    public void run() {
        infoTextBuild = new StringBuilder();
        double x = startX;
        int n = calcN(stepX);
        infoTextBuild.append("\n\nКількість кроків табулювання: ").append(n);
        points = createArr(n);
        for (int i = 0; i < n; i++) {
            points[i] = new Point((float)x, (float)calcY(x,2.4));
            x+=stepX;
            //System.out.println(x);
        }
        int maxE_x = getMinMaxNum(points, true, false), minE_x = getMinMaxNum(points, false, false), maxE_y = getMinMaxNum(points, true, true), minE_y = getMinMaxNum(points, false, true);
        double[] xN = getSumAndArif(points, false), yN = getSumAndArif(points, true);
        infoTextBuild.append("\nМінімальний елемент Х = ").append(round(points[minE_x].x, 3)).append(" Максимальний елемент Х = ").append(round(points[maxE_x].x,3)).append("\nМінімальний елемент Y = ").append(round(points[minE_y].y,3)).append(" Максимальни елемент Y = ").append(round(points[maxE_y].y,3));
        infoTextBuild.append("\nСума елементів Х = ").append(round(xN[0],3)).append(" Сумма елементів Y = ").append(round(yN[0],3)).append("\nСереднє арифметичне елементів Х = ").append(round(xN[1],3)).append("\nСереднє арифметичне елементів Y = ").append(round(yN[1], 3));
        maxY = points[maxE_y].y; minY = points[minE_y].y;
        InfoText.setText(infoTextBuild.toString());
        InfoText.setFont(new Font(20));
        InfoText.setVisible(true);
    }

    private int getMinMaxNum(Point[] arr, boolean max, boolean isYArray) {
        double buff = 0;
        if (isYArray) {
            buff = arr[0].y;
        } else {
            buff = arr[0].x;
        }
        int n = 0;
        for (int i = 1; i < arr.length; i++) {
            if (isYArray) {
                if (arr[i].y < buff && !max) {
                    buff = arr[i].y;
                    n = i;
                } else if (buff < arr[i].y && max) {
                    buff = arr[i].y;
                    n = i;
                }
            } else {
                if (arr[i].x < buff && !max) {
                    buff = arr[i].x;
                    n = i;
                } else if (buff < arr[i].x && max) {
                    buff = arr[i].x;
                    n = i;
                }
            }
        }
        return n;
    }

    protected double[] getSumAndArif(Point[] arr, boolean isYArray) {
        double[] out = new double[2];
        double buff = 0;
        for (int i = 0; i < arr.length; i++) {
            if (isYArray) {
                buff += arr[i].y;
            } else {
                buff += arr[i].x;
            }
        }
        out[0] = buff;
        out[1] = buff/arr.length;
        return out;
    }

    protected Point[] createArr(int amountOfElem) {
        return new Point[amountOfElem];
    }

    protected int calcN(double step) {
        int amount = 0;
        for (double i = startX; i <= endX; i+=step) {
            amount++;
        }
        return amount;
    }

    protected double calcY(double x, double a) {
        if (x > a) {
            return x*Math.sqrt(x-a);
        } else if (x == a) {
            return (x * Math.sin(a) * x);
        } else if (x < a) {
            return Math.pow(Math.exp(0),-a*x) * Math.cos(a) * x;
        } else {
            return 0;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

