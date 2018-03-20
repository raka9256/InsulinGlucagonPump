package Model;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import View.GUI;
/**
 * Created by syka on 6/23/16.
 */
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class AnimatedLineChart {

    private static final int MAX_DATA_POINTS = 15;
    private int xSeriesData = 0;
    private XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
    private ExecutorService executor;
    private ConcurrentLinkedQueue<Number> dataQ1 = new ConcurrentLinkedQueue<>();
    private AreaChart<Number, Number> lineChart;
    private NumberAxis xAxis;
    private MathematicalModel mathematicalModel;
	private boolean insulinInjected;
	private boolean glucagonInjected;
	GUI guiInst;
	private int actualGL;
	private long injectedTime;
	private final int MONITORING_GLUCOSE_TIME=2000;
	Battery battery;
	InsulinReservoir insulin;
	AudioPlayer02 audioplayer02 = new AudioPlayer02();
	PatientData pdata;
	
	public AnimatedLineChart(boolean insulinInjected, boolean glucagonInjected,GUI gui, 
			Battery batteryInst, InsulinReservoir insulinInst, PatientData patientData) {
		this.insulinInjected = insulinInjected;
		this.glucagonInjected = glucagonInjected;
		guiInst=gui;
		battery = batteryInst;
		insulin = insulinInst;
		pdata=patientData;
	}

	private void init(JFXPanel primaryStage) {
        xAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setMinorTickVisible(false);
        xAxis.setLabel("Time(minutes)");

		NumberAxis yAxis = new NumberAxis(60, 350, 10);
		yAxis.setLabel("Blood Sugar Level(mg/dl)");

		// Create a LineChart
		lineChart = new AreaChart<Number, Number>(xAxis, yAxis) {
			// Override to remove symbols on each data point
			@Override
			protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
			}
		};

		lineChart.setAnimated(false);
		lineChart.setTitle("Glucose level chart");
		lineChart.setHorizontalGridLinesVisible(true);

		// Set Name for Series
		series1.setName("Glucose level monitoring");

		// Add Chart Series
		lineChart.getData().add(series1);

        primaryStage.setScene(new Scene(lineChart,300,200));
        //fxPanel.setScene(primaryStage.getScene());
        
    }

	public JFXPanel createPanel() {
		JFXPanel fxPanel = new JFXPanel();
		init(fxPanel);
		mathematicalModel = new MathematicalModel();

		executor = Executors.newCachedThreadPool(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(true);
				return thread;
			}
		});

		AddToQueue addToQueue = new AddToQueue();
		executor.execute(addToQueue);
		// -- Prepare Timeline
		prepareTimeline();
		return fxPanel;
	}

	private class AddToQueue implements Runnable {
		public void run() {
			try {
				// add a item of random data to queue
				actualGL = mathematicalModel.glucoseLevelChange(110, 120, injectedTime, glucagonInjected,
						insulinInjected,guiInst,battery,insulin,pdata);
				dataQ1.add(actualGL);
				if (actualGL > 250) {
				    String song="C:\\Users\\RAKA\\Desktop\\bewafa.wav";
				    audioplayer02.playAudio(song);
					Platform.runLater(() -> lineChart.setTitle("WARNING! Glucose level is high : " + actualGL));		               
				} else if (actualGL < 75) {
					String song="C:\\Users\\RAKA\\Desktop\\bewafa.wav";
				    audioplayer02.playAudio(song);
					Platform.runLater(() -> lineChart.setTitle("WARNING! Glucose level is low : " + actualGL));
				} else {
					Platform.runLater(() -> lineChart.setTitle("Glucose level: " + actualGL));
				}
				Thread.sleep(MONITORING_GLUCOSE_TIME);
				executor.execute(this);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

    //-- Timeline gets called in the JavaFX Main thread
    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

	private void addDataToSeries() {
		for (int i = 0; i < 20; i++) { // -- add 20 numbers to the plot+
			if (dataQ1.isEmpty())
				break;
			series1.getData().add(new XYChart.Data<>(xSeriesData++, dataQ1.remove()));
		}
		// remove points to keep us at no more than MAX_DATA_POINTS
		if (series1.getData().size() > MAX_DATA_POINTS) {
			series1.getData().remove(0, series1.getData().size() - MAX_DATA_POINTS);
		}
		// update
		xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
		xAxis.setUpperBound(xSeriesData - 1);
	}

	public void setInsulinInjected(boolean insulinInjected) {
		this.insulinInjected = insulinInjected;
	}

	public void setGlucagonInjected(boolean glucagonInjected) {
		this.glucagonInjected = glucagonInjected;
	}

	public int getActualGL() {
		return actualGL;
	}

	public void setActualGL(int actualGL) {
		this.actualGL = actualGL;
	}

	public long getInjectedTime() {
		return injectedTime;
	}

	public void setInjectedTime(long injectedTime) {
		this.injectedTime = injectedTime;
	}
}