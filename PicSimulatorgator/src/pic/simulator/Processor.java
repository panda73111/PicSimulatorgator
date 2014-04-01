/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic.simulator;

import pic.simulator.parser.*;

/**
 *
 * @author hudini
 */
public class Processor {

    private int progCounter;
    private Program picProgram;
    private boolean isInterrupted;

    public Command fetch(int cmdIndex) {
        return null;
    }

    public void executeCmd(Command cmd) {

    }

    public void executeProg() {
        while (progCounter < picProgram.length())
        {
            if (isInterrupted)
            {
                //TODO
            }
            Command cmd = fetch(progCounter);
            executeCmd(cmd);
            progCounter++;
            //TODO repaint
        }
    }
}
