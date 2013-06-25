package fr.automated.trading.systems.utils.utils;

import java.io.*;
import java.util.ArrayList;

public class ExtendedFile extends java.io.File {

    private static final long serialVersionUID = 1L;

    public ExtendedFile(String filename) {
        super(filename);
    }

    public int countLines() {
        try {
            LineNumberReader lineCounter = new LineNumberReader(new InputStreamReader(new FileInputStream(super.getAbsolutePath())));
            while ((lineCounter.readLine()) != null);
            lineCounter.close();
            return lineCounter.getLineNumber();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return 0;
        }
    }

    public ArrayList<String> extractLines(int begin, int count) {
        try {
            ArrayList<String> results = new ArrayList<>();
            LineNumberReader lineCounter = new LineNumberReader(new InputStreamReader(new FileInputStream(super.getAbsolutePath())));

            int counter = 0;
            while ((lineCounter.readLine()) != null) {
                counter++;
                if(counter == begin)
                    break;
            }

            while(count > 0) {
                results.add(lineCounter.readLine());
                count--;
            }

            lineCounter.close();
            return results;

        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }

    public void append(String text) {
        BufferedWriter bufWriter = null;
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(super.getAbsolutePath(), true);
            bufWriter = new BufferedWriter(fileWriter);
            bufWriter.newLine();
            bufWriter.write(text);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        finally {

            try {
                bufWriter.close();
                fileWriter.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    public boolean copy(String destinationPath) {

        boolean result = false;

        java.io.File destination = new java.io.File(destinationPath);
        java.io.File source = new java.io.File(super.getAbsolutePath());

        java.io.FileInputStream sourceFile = null;
        java.io.FileOutputStream destinationFile = null;

        try {

            destination.createNewFile();
            sourceFile = new java.io.FileInputStream(source);
            destinationFile = new java.io.FileOutputStream(destination);

            byte buffer[] = new byte[512*1024];
            int nbLecture;

            while((nbLecture = sourceFile.read(buffer)) != -1 )
                destinationFile.write(buffer, 0, nbLecture);

            result = true;

        } catch(Exception e) {
            e.printStackTrace();
        }

        finally {

            try {
                sourceFile.close();
            } catch(Exception e) {
                e.printStackTrace();
            }

            try {
                destinationFile.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return(result);
    }

}