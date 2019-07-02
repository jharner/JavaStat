package wvustat.simulation.fivestep;

import wvustat.simulation.model.IGenerator;
import wvustat.simulation.model.StatComputer;

import java.awt.Font;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Jul 10, 2004
 * Time: 2:56:14 PM
 * To change this template use Options | File Templates.
 */
public class PrintManager
{
    private FiveStepMediator mediator;
    private StatComputer statComputer;
    private Font titleFont = new Font("Arial", Font.BOLD, 14);
    private Font headerFont = new Font("Arial", Font.BOLD, 12);
    private Font normalFont = new Font("Arial", Font.PLAIN, 12);
    private IGenerator model;
    private PageLayout pageLayout;

    public PrintManager(FiveStepMediator mediator, StatComputer statComputer)
    {
        this.mediator = mediator;
        this.model = mediator.getModel();
        this.statComputer = statComputer;
        pageLayout=new PageLayout();
        preparePage();
    }

    public void print()
    {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(pageLayout);
        // Step 3: Find print services.
        //PrintService[] services = PrinterJob.lookupPrintServices();
        //if (services.length > 0)
        {
            try
            {
                //pj.setPrintService(services[0]);
                // Step 2: Pass the settings to a page dialog and print dialog.
                if (pj.printDialog())
                {
                    // Step 4: Update the settings made by the user in the dialogs.
                    // Step 5: Pass the final settings into the print request.

                    pj.print();
                    //JOptionPane.showMessageDialog(mediator.getModelInputPanel().getRootPane(), "A report has been printed on " + services[0].getName());

                }
            }
            catch (PrinterException pe)
            {
                System.err.println(pe);
            }
        }
        /*
        else
        {
            JOptionPane.showMessageDialog(mediator.getModelInputPanel().getRootPane(), "No printer can be found on your computer!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        */

    }

    private void preparePage()
    {
        pageLayout.addLine("Five Step Simulation", titleFont);

        pageLayout.addLine(30);

        pageLayout.addLine("Model", headerFont);

        pageLayout.addLine(model.getModelDescription(), normalFont);

        if (!(model.isDepletable()))
        {
            return;
        }

        pageLayout.addLine(10);

        String[] headers=new String[model.getObservationAttributeCount()+1];
        headers[0]="Count";
        String[] labels = model.getAttribueNames();
        for (int i = 0; i < model.getObservationAttributeCount(); i++)
        {

            headers[i+1]=labels[i];
        }
        pageLayout.addLine(headers, headerFont);


        int[] freqs=model.getCount();
        for (int i = 0; i < model.getOutcomeCount(); i++)
        {
            String[] cells=new String[headers.length];

            cells[0]=String.valueOf(freqs[i]);
            for (int j = 0; j < model.getObservationAttributeCount(); j++)
            {
                Object[] values = model.getAttributes(j);
                cells[j+1]=String.valueOf(values[i]);
            }
            pageLayout.addLine(cells, normalFont);
        }
        pageLayout.addLine(10);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);


        pageLayout.addLine("Define the Sample", headerFont);

        pageLayout.addLine(IGenerator.trial_names[model.getTrialType()],normalFont);
        if (model.getTrialType() == IGenerator.DRAW_N_W_REP || model.getTrialType() == IGenerator.DRAW_N_WO_REP)
        {
            String str = String.valueOf(mediator.getFiveStep().getNumOfDraws());

            pageLayout.addLine("n= "+str, normalFont);
        }
        else if(model.getTrialType()==IGenerator.DRAW_UNTIL_X)
        {
            pageLayout.addLine("X="+String.valueOf(model.getGoal()), normalFont);
        }

        pageLayout.addLine(10);

        pageLayout.addLine("Statistics of Interest", headerFont);

        pageLayout.addLine(statComputer.getStatType(), normalFont);

        pageLayout.addLine(10);

        if (StatComputer.getEventIndex() > 0)
        {

            pageLayout.addLine("Event", headerFont);
            String eventChoice = StatComputer.eventChoices[StatComputer.getEventIndex()];

            pageLayout.addLine(eventChoice, normalFont);
            if (!Double.isNaN(statComputer.getA()))
            {

                pageLayout.addLine("a= "+String.valueOf(statComputer.getA()), normalFont);
            }
            if (!(Double.isNaN(statComputer.getB())))
            {

                pageLayout.addLine("b= "+String.valueOf(statComputer.getB()), normalFont);
            }

            pageLayout.addLine(10);
        }


        pageLayout.addLine("Simulation Results", headerFont);
        headers=new String[]{"Statistics of Interest", "Summary"};

        pageLayout.addLine(headers, headerFont);

        String[] cells=new String[2];
        cells[0]="# of Simulations";

        cells[1]=String.valueOf(statComputer.getStats().length);

        pageLayout.addLine(cells, normalFont);

        cells=new String[2];
        cells[0]="Mean";
        cells[1]=nf.format(statComputer.getAverage());
        pageLayout.addLine(cells, normalFont);

        cells=new String[2];
        cells[0]="Standard Deviation";
        cells[1]= nf.format(statComputer.getSD());

        pageLayout.addLine(cells, normalFont);

        cells=new String[2];
        cells[0]="Median";
        cells[1]=nf.format(statComputer.getQuantile(0.5));
        pageLayout.addLine(cells, normalFont);

        cells=new String[2];
        cells[0]="Interquartile Range";
        cells[1]=nf.format(statComputer.getIQR());
        pageLayout.addLine(cells, normalFont);

        cells=new String[2];
        cells[0]="Min";
        cells[1]=nf.format(statComputer.getMin());

        pageLayout.addLine(cells, normalFont);

        cells=new String[2];
        cells[0]="Max";
        cells[1]=nf.format(statComputer.getMax());
        pageLayout.addLine(cells, normalFont);
        pageLayout.addLine(10);

        if(StatComputer.getEventIndex()>0)
        {
            int successCount=statComputer.getSuccessCount();
            int totalCount=statComputer.getStats().length;


            cells=new String[]{"Event of Interest", "Count", "Experimental Probability"};
            pageLayout.addLine(cells, headerFont);

            cells=new String[3];
            cells[0]="Success";
            cells[1]=String.valueOf(statComputer.getSuccessCount());
            cells[2]=nf.format(1.0*statComputer.getSuccessCount()/statComputer.getStats().length);

            pageLayout.addLine(cells, normalFont);

            cells=new String[3];
            cells[0]="Failure";
            cells[1]=String.valueOf(totalCount-successCount);
            cells[2]=nf.format(1.0*(totalCount-successCount)/totalCount);

            pageLayout.addLine(cells, normalFont);

            cells=new String[3];
            cells[0]="Total";
            cells[1]=String.valueOf(totalCount);
            cells[2]="1.00";
            pageLayout.addLine(cells, normalFont);

        }

        pageLayout.addLine(20);
        pageLayout.addLine(mediator.getStatPanel().getHistogram(), 400, 200);
    }
}
