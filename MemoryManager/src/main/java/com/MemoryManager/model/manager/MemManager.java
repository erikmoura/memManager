package com.MemoryManager.model.manager;

import com.MemoryManager.model.config.Configuration;
import com.MemoryManager.model.memory.MainMemory;
import com.MemoryManager.model.memory.SecondaryMemory;
import com.MemoryManager.model.memory.TLB;
import com.MemoryManager.model.memory.Translator;
import com.MemoryManager.model.systemResources.*;
import com.MemoryManager.model.systemResources.Process;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class MemManager {

    private Configuration config = Configuration.getInstance();
    private MainMemory mainMemo = new MainMemory();
    private SecondaryMemory secMemo = new SecondaryMemory();
    private TLB tlb = new TLB();
    private Translator translator = new Translator();
    private ArrayList<PageTable> pageTables = new ArrayList<>();
    private ReplacementPolicy replacementPolicy = new ClockPolicy();

    private MemManager(){}
    private static volatile MemManager instance;
    public static MemManager getInstance(){
        if( instance == null){
            synchronized(MemManager.class){
                if(instance == null){
                    instance = new MemManager();
                }
            }
        }
        return instance;
    }

    public void initialize(){
        this.mainMemo.setSize(config.getMainMemo_size());
        this.mainMemo.prepareFrames(config.getPageFrame_size());
        this.secMemo.setSize(config.getSecMemo_size());
        this.secMemo.prepareFrames(config.getPageFrame_size());
    }

    public void handleEntry(String entryLine){
        String[] lineData = entryLine.split(" ");

        String processId = lineData[0];
        String instruction = lineData[1];

        // common variables
        int processNumber = Integer.parseInt(processId.substring(1));;
        int processSize;
        int numberOfPages;
        ArrayList<Page> processPages;
        Process process;
        PageTable processPageTable;
        int pageTableIdx;
        Process owner;
        int frameStart;
        int offSet;
        int realAddress;
        Page desiredPage;
        int desiredFrame;

        switch(instruction){
            case "P":
                break;
            case "I":
                break;
            case "C":
                // creating a process
                // they're first put in the secondary memory
                processSize = Integer.parseInt(lineData[2]);
                numberOfPages = (processSize + config.getPageFrame_size() - 1) / config.getPageFrame_size();
                processPages = new ArrayList<>();
                for(int i = 0; i < numberOfPages; i++){
                    processPages.add(new Page(i, processNumber));
                }

                process = new Process(processNumber, State.READY_SUSPEND, processSize, numberOfPages, processPages);
                processPageTable = new PageTable(process);
                this.pageTables.add(processPageTable);
                this.secMemo.receiveProcessPages(processPages);
                break;
            case "R":
                // implementing the reading operation
                // the translator might be used here

                // finding the process
                owner = null;
                for (PageTable pageTable : this.pageTables) {
                    if (pageTable.getOwner().getId() == processNumber) {
                        owner = pageTable.getOwner();
                        break;
                    }
                }

                if(owner != null){
                    // finding the page that contains the address (logical) and the offset
                    // first position of the array contains the start of the frame, second position contains the offset
                    ArrayList<Integer> addressData = this.translator.findPageAddress(Integer.parseInt(lineData[2]), this.config.getPageFrame_size());

                    // checking if the page is in the main memory
                    frameStart = addressData.get(0);
                    desiredPage = owner.getPages().get(frameStart);
                    if(desiredPage.isPresenceBit()){
                        // if it is, use the offset to translate the address and access the real address to execute the read operation
                        offSet = addressData.get(1);
                        desiredFrame = this.mainMemo.findFrame(desiredPage);
                        if(desiredFrame != -1){
                            realAddress = (desiredFrame * config.getPageFrame_size()) + offSet;
                            owner.getPages().get(frameStart).setUseBit(true);
                        }

                    }
                    else{
                        // if it isn't, bring the page to the main memory applying the replacement policy (clock)
                        // after that, use the offset to translate the address and access the real address to execute the read operation
                        // remember to alter the presence bit and use bit
                        this.secMemo.removePage(desiredPage);
                        this.replacementPolicy.replace(desiredPage, this.mainMemo, this.secMemo);
                        offSet = addressData.get(1);
                        desiredFrame = this.mainMemo.findFrame(desiredPage);
                        if(desiredFrame != -1){
                            realAddress = (desiredFrame * config.getPageFrame_size()) + offSet;
                        }
                    }

                }
                break;
            case "W":
                break;
            case "T":
                // finding the process and killing it

                // checking the pages to find their owner
                pageTableIdx = -1;
                owner = null;
                for(int i = 0; i < this.pageTables.size(); i++){
                    if(this.pageTables.get(i).getOwner().getId() == processNumber){
                        pageTableIdx = i;
                        owner = this.pageTables.get(i).getOwner();
                        break;
                    }
                }

                // removing the process pages from the memory
                if(owner != null){
                    for(Page p : owner.getPages()){
                        if(p.isPresenceBit()){
                            mainMemo.removePage(p);
                        }
                        else{
                            secMemo.removePage(p);
                        }
                    }
                }

                // deleting the process' page table and the process itself
                if(pageTableIdx != -1){
                    this.pageTables.remove(pageTableIdx);
                }
                else{
                    // treat exception
                }
                break;
            default:
                // warn and ask for an adequate entry
                break;
        }

    }


}
