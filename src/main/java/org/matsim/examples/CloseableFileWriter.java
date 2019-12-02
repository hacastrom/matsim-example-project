package org.matsim.examples;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.matsim.core.utils.io.IOUtils;

public class CloseableFileWriter {
	
	private String path;
	
	public CloseableFileWriter() {
		path = null;
	}
	
	public void setPath (String string) {
		this.path = string;
	}
	
	public String getPath () {
		return this.path;
	}
	
	public void writeFileCloseable(ArrayList<String> Ids){
        BufferedWriter bw = IOUtils.getBufferedWriter(this.path);
        try {
            for (int i = 0;i< Ids.size();i++){
                bw.write(Ids.get(i));
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }	
	
	public void writeFileFinalize(ArrayList<String> Ids){
        
        try (BufferedWriter bw = IOUtils.getBufferedWriter(this.path)) {
            for (int i = 0;i< Ids.size();i++){
                bw.write(Ids.get(i));
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }	
	
	@Override
	protected void finalize() {
		System.out.println("Finalizer was activated");
	}
	
}
