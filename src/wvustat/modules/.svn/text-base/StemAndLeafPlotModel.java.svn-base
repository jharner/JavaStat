package wvustat.modules;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.*;

import javax.swing.event.*;

public class StemAndLeafPlotModel {
	private double[] data;
	private Bin[] bins ;
	private int targetLength;
	
	private List listeners ;

	protected static final int DEFAULT_TARGET_LENGTH = 15;
	private static final double LOG2 = Math.log(2) / Math.log(10);
	private static final double LOG5 = Math.log(5) / Math.log(10);

	public StemAndLeafPlotModel(double[] data, int targetLength) {
		double[] theData ;
		if (data==null) {
			theData = new double[0];
		} else {
			theData = data ;
		}
		newData(theData);
		this.targetLength = targetLength ;
		listeners = new ArrayList();
		compute();
	}
	
	public StemAndLeafPlotModel(double[] data) {
		this(data, DEFAULT_TARGET_LENGTH);
	}
	
	public StemAndLeafPlotModel() {
		this(new double[0]);
	}

	private void compute() {
		if (data.length>0) {
			final double min = this.data[0];
			final double max = this.data[data.length-1];
			final double range = max - min;
			final double exactStep = range / targetLength;
			final double logExactStep = Math.log(exactStep) / Math.log(10);
			int stemExp = (int) Math.floor(logExactStep);
			
			
			// Actual step will be one of 10^exactStepExp, 2*10^exactStepExp, or
			// 5*10^exactStepExp
			// First calculate the two either side of exactStep
			// Then find which of those two gives a length closest to targetLength
	
			final double logResid = logExactStep - stemExp;
			final BinLength lowerBinLength ;
			final BinLength upperBinLength ;
			final int lowerBinExp ;
			final int upperBinExp ;
			if (logResid >= LOG5) {
				lowerBinLength = BinLength.HALF_UNIT ;
				lowerBinExp = stemExp + 1 ;
				upperBinLength = BinLength.UNIT ;
				upperBinExp = stemExp + 1 ;
			} else if (logResid >= LOG2) {
				lowerBinLength = BinLength.DOUBLE_UNIT ;
				lowerBinExp = stemExp ;
				upperBinLength = BinLength.HALF_UNIT ;
				upperBinExp = stemExp + 1 ;
			} else {
				lowerBinLength = BinLength.UNIT ;
				lowerBinExp = stemExp ;
				upperBinLength = BinLength.DOUBLE_UNIT ;
				upperBinExp = stemExp ;
			}
			double lowerLength = range / (upperBinLength.asDouble(upperBinExp));
			double upperLength = range / (lowerBinLength.asDouble(lowerBinExp));
			BinLength binLength ;
			if (upperLength - targetLength < targetLength - lowerLength) {
				binLength = lowerBinLength;
				stemExp = lowerBinExp ;
			} else {
				binLength = upperBinLength ;
				stemExp = upperBinExp ;
			}
			
			Bin startBin = Bin.createBinFor(data[0], stemExp, binLength);
			List binList = new ArrayList();
			Bin bin = startBin ;
			while (! (bin.inBin(data[data.length-1]))){
				binList.add(bin);
				bin = bin.nextBin();
			}
			Bin lastBin = (Bin) binList.get(binList.size()-1);
			if (! lastBin.inBin(data[data.length-1])) {
				binList.add(lastBin.nextBin());
			}
			bins = new Bin[binList.size()];
			for (int i=0; i<bins.length; i++ ){
				bins[i] = (Bin)binList.get(i);
			}
		} else {
			bins = new Bin[0];
		}
		fireChangeEvent();
	}
	
	public void addChangeListener(ChangeListener cl) {
		if (! listeners.contains(cl)){
			listeners.add(cl);
		}
	}
	
	public void removeChangeListener(ChangeListener cl) {
		listeners.remove(cl);
	}
	
	public void fireChangeEvent() {
		ChangeEvent evt = new ChangeEvent(this);
		for (ListIterator it = listeners.listIterator(listeners.size()); it.hasPrevious(); ){
			ChangeListener cl = (ChangeListener)it.previous();
			cl.stateChanged(evt);
		}
	}

	public synchronized int getTargetLength() {
		return targetLength;
	}

	public synchronized void setTargetLength(int targetLength) {
		this.targetLength = targetLength;
		compute();
	}

	public synchronized int getNumberOfBins(){
		return bins.length;
	}
	
	public synchronized int getExponent() {
		if (bins.length>0) {
			return bins[0].getExponent();
		} else {
			return 0 ;
		}
	}

	public synchronized double[] getData() {
		double[] d = new double[data.length];
		System.arraycopy(data, 0, d, 0, data.length);
		return data;
	}
	
	public synchronized void setData(double[] newData) {
		newData(newData);
		compute();
	}
	
	private void newData(double[] newData) {
		List dataList = new ArrayList();
		for (int i=0; i<newData.length; i++) {
			if (! (Double.isNaN(newData[i]) || Double.isInfinite(newData[i])) ){
				dataList.add(new Double(newData[i]));
			}
		}
		this.data = new double[dataList.size()];
		for (int i=0; i<dataList.size(); i++){
			this.data[i] = ((Double)dataList.get(i)).doubleValue();
		}
		Arrays.sort(this.data);
	}
	
	public synchronized List getDataAsList() {
		List l = new ArrayList(data.length);
		for (int i=0; i<data.length; i++) {
			l.add(new Double(data[i]));
		}
		return l ;
	}

	public synchronized void output(PrintWriter out) {
		if (data.length>0) {
			int maxCols = Math.max(bins[0].toString().length(), bins[bins.length-1].toString().length());
			
			for (int i=0, dataIndex=0 ; i<bins.length; i++) {
				out.print(padTo(bins[i].toString(), maxCols, Justify.RIGHT) + " | ");
				while (dataIndex<data.length && bins[i].inBin(data[dataIndex])){
					out.print(bins[i].getDisplayForValue(data[dataIndex]));
					dataIndex++ ;
				}
				out.println();
			}
		} else {
			out.print(" ");
		}
		out.flush();
	}

	public synchronized String toString() {
		CharArrayWriter writer = new CharArrayWriter();
		output(new PrintWriter(writer));
		writer.close();
		return writer.toString();
	}

	private String padTo(String s, int cols, Justify justify) {
		StringBuffer sb = new StringBuffer();
		if (justify == Justify.LEFT) {
			sb.append(s);
		}
		for (int i = 0; i < cols - s.length(); i++) {
			sb.append(" ");
		}
		if (justify == Justify.RIGHT) {
			sb.append(s);
		}
		return sb.toString();
	}
	
	private static class Bin {
		private final int startValue ;
		private final int exponent ;
		private final BinLength length ;
		public Bin(int exponent, BinLength length, int startValue) {
			this.exponent = exponent;
			this.length = length;
			this.startValue = startValue;
		}
		public static Bin createBinFor(double value, BinLength length) {
			int exponent = (int) (Math.floor(Math.log(Math.abs(value))/Math.log(10))) ;
			return createBinFor(value, exponent, length);

		}
		public static Bin createBinFor(double value, int exp, BinLength length) {
			int exponent = exp ;
			double scaleValue = value ;
			for (int i=0; i<exponent; i++) {
				scaleValue = scaleValue / 10 ;
			}
			for (int i=0; i<-exponent; i++) {
				scaleValue = scaleValue * 10 ;
			}
			int start = (int) (Math.floor(scaleValue*length.getRoundFactor()/length.leadingSignificantDigit())) * length.leadingSignificantDigit() ;
			return new Bin(exponent, length, start);
		}
		
		public synchronized int getStartValue() {
			return startValue;
		}
		public synchronized int getExponent() {
			return exponent;
		}
		public synchronized BinLength getLength() {
			return length;
		}
		public Bin nextBin() {
			return new Bin(exponent, length, startValue+length.leadingSignificantDigit());
		}
		public Bin previousBin() {
			return new Bin(exponent, length, startValue-length.leadingSignificantDigit());
		}
		public boolean inBin(double value) {
			long start = startValue ;
			long end ;
			end = start + length.leadingSignificantDigit();
			for (int i=0; i<(exponent-length.getLogRoundFactor()); i++) {
				start = start * 10 ;
				end = end * 10 ;
			}
			
			double testValue = value ;
			for (int i=0; i<-(exponent-length.getLogRoundFactor()) ; i++) {
				testValue = testValue * 10 ;
			}
			return start <= testValue && testValue < end ;
		}
		public synchronized int getDisplayStart() {
			return length.getDisplayForStart(startValue);
		}
		public String toString() {
			int dispStart = getDisplayStart();
			if (startValue<0 && dispStart==0) {
				return "-0";
			}
			else return Integer.toString(dispStart);
		}
		public synchronized int getDisplayForValue(double value) {
//			int exp = exponent + length.getLogRoundFactor();
			double testValue = value ;
			for (int i=0; i<exponent-1; i++) {
				testValue = testValue / 10 ;
			}
			for (int i=0; i<1-exponent; i++) {
				testValue = testValue * 10 ;
			}
			return (int)(Math.abs(testValue)+0.5) % 10 ;
		}
	}
	
	// Typesafe enums

	private static class Justify {
		private Justify() {
		}

		public static final Justify LEFT = new Justify();
		public static final Justify RIGHT = new Justify();
	}
	
	private static class BinLength {
		private final int leadingSignificantDigit ;
		private final int logRoundFactor ;
		private final int roundFactor ;
		private BinLength(int leadingSignificantDigit, int logRoundFactor) {
			this.leadingSignificantDigit = leadingSignificantDigit;
			this.logRoundFactor = logRoundFactor ;
			int rf = 1 ;
			for (int i=0; i<logRoundFactor; i++) {
				rf*=10 ;
			}
			this.roundFactor = rf ;
		}
		public static final BinLength UNIT = new BinLength(1, 0);
		public static final BinLength HALF_UNIT = new BinLength(5, 1);
		public static final BinLength DOUBLE_UNIT = new BinLength(2, 0);
		public int leadingSignificantDigit() {
			return leadingSignificantDigit;
		}
		public int getDisplayForStart(int start) {
			int startVal = (int) Math.floor(start/roundFactor);
			int end = (int) Math.floor((start+leadingSignificantDigit)/roundFactor) ;
			if (end<=0) {
				return end ;
			} else {
				return startVal ;
			}
		}
		public int getRoundFactor() {
			return roundFactor ;
		}
		public int getLogRoundFactor() {
			return logRoundFactor ;
		}
		public double asDouble(int exponent) {
			double value = leadingSignificantDigit ;
			for (int i=0; i<exponent-logRoundFactor; i++) {
				value = value * 10 ;
			}
			for (int i=0; i<-(exponent-logRoundFactor); i++) {
				value = value / 10 ;
			}
			return value ;
		}
	}

	// test driver
	public static void main(String[] args) {
		
		if (args.length > 0 && args[0].equalsIgnoreCase("-h")) {
			System.out
					.println("Usage:\n\tjava StemAndLeafPlotModel [gaussian|uniform|log_gaussian|log_uniform [numberOfSamples mean|min sd|max]]");
			System.exit(0);
		}
		String type;
		if (args.length == 0) {
			type = "gaussian";
		} else {
			type = args[0].toLowerCase();
		}
		int samples;
		double min, max, mean, sd;
		if (args.length <= 1) {
			samples = 100;
			if (type.endsWith("gaussian")) {
				min = max = Double.NaN;
				sd = 1;
				mean = 0;
			} else {
				mean = sd = Double.NaN;
				min = 0;
				max = 1;
			}
		} else {
			samples = Integer.parseInt(args[1]);
			if (type.endsWith("gaussian")) {
				mean = Double.parseDouble(args[2]);
				sd = Double.parseDouble(args[3]);
				min = max = Double.NaN;
			} else { // uniform
				min = Double.parseDouble(args[2]);
				max = Double.parseDouble(args[3]);
				mean = sd = Double.NaN;
			}
		}
		double[] data = new double[samples];
		Random rng = new Random();
		for (int i = 0; i < samples; i++) {
			double value;
			if (type.endsWith("gaussian")) {
				value = rng.nextGaussian() * sd + mean;
			} else {
				value = rng.nextDouble() * (max - min) + min;
			}
			if (type.startsWith("log_")) {
				value = Math.log(value);
			}
			data[i] = value;
		}
		StemAndLeafPlotModel model = new StemAndLeafPlotModel(data);
		for (int i=0; i<data.length; i++) {
			System.out.print(data[i]);
			if (i<data.length-1){
				System.out.print(", ");
			}
			System.out.println();
		}
		System.out.println(model.getDataAsList());
		System.out.println("target length: "+model.getTargetLength());
		System.out.println("actual length: "+model.getNumberOfBins());
		System.out.println("Exponent: " + model.getExponent());
		System.out.println(model);
	}

}

