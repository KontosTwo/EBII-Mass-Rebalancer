package massbalancer;

import massbalancer.Balancer.Balancer;
import massbalancer.Balancer.BalancerDefinition;
import massbalancer.Balancer.BalancerException;
import massbalancer.Unit.Unit;
import massbalancer.Unit.UnitReader;
import massbalancer.Unit.UnitWriter;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class Runner {
    public static final String EDU = "export_descr_unit.txt";

    public static void main(String[] args){
        if(args.length < 2){
            System.err.println(String.format("At least 2 arguments provided, but only %d received", args.length));
            return;
        }

        if(args[0].equals(args[1])){
            System.err.println(String.format("%s is both the input and output path. You want to make the output different just to be safe"));
            return;
        }

        final String filePath = args[0];
        String fileName;
        try{
            fileName = Path.of(filePath).getFileName().toString();
        }catch (final Exception e){
            System.err.println(String.format("The input path provided %s is not a valid path",filePath));
            return;
        }
        if(!fileName.equals(EDU)){
            System.err.println(String.format("File name does not match %s", EDU));
            return;
        }

        FileInputStream fis;
        try {
            fis = new FileInputStream(filePath);
        }catch(final FileNotFoundException e){
            System.err.println(String.format("Could not find %s file at %s", EDU, filePath) );
            return;
        }
        final BufferedReader in = new BufferedReader(new InputStreamReader(fis));

        Map<String, Unit> unitNameToUnit = null;
        try {
            unitNameToUnit = UnitReader.read(in);
        }catch (IOException e){
            System.err.println("Error while processing file");
            return;
        }finally{
            try{
                in.close();
            }catch (final IOException e){
                e.printStackTrace();
            }
        }

        final String outputFilePath = args[1];

        FileInputStream fisWrite;
        try {
            fisWrite = new FileInputStream(filePath);
        }catch(final FileNotFoundException e){
            System.err.println(String.format("Could not find %s file at %s", EDU, filePath) );
            return;
        }
        final BufferedReader inWrite = new BufferedReader(new InputStreamReader(fisWrite));
        BufferedWriter out;
        try{
             out = new BufferedWriter(new FileWriter(outputFilePath));
        }catch(final IOException e){
            System.err.println(String.format("The output path provided at %s is not a valid path",outputFilePath));
            try {
                inWrite.close();
            } catch (IOException innerE) {
                e.printStackTrace();
            }
            return;
        }

        final List<Balancer> balancers = BalancerDefinition.useDefault();

        try{
            UnitWriter.write(inWrite, out,unitNameToUnit,balancers);
        } catch (BalancerException e) {
            System.err.println(String.format("Error occurred while calculating and replacing mass values: %s", e.getMessage()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error occurred while writing: %s", e.getMessage()));
            return;
        }finally {
            try {
                out.close();
                inWrite.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
