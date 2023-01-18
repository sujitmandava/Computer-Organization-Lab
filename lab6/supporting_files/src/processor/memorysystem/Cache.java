package processor.memorysystem;

import processor.Processor;
import generic.MemoryReadEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;
import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;

public class Cache implements Element {

    CacheLine[] cacheLines;
    int numberOfLines;
    int index;
    int tag;
    int data;
    int cacheType; // 0 for I; 1 for D
    Processor containingProcessor;

    public Cache(int noOfCacheLines, Processor containingProcessor, int cacheType) {
        cacheLines = new CacheLine[noOfCacheLines]; // TODO: initialize all lines manually

        for (int i = 0; i < noOfCacheLines; i++) {
            cacheLines[i] = new CacheLine();
        }

        numberOfLines = noOfCacheLines;
        this.containingProcessor = containingProcessor;
        this.cacheType = cacheType;
    }

    public int cacheRead(int address) {
        index = address % numberOfLines;
        tag = address / numberOfLines;

        if (tag == cacheLines[index].getTag()) {
            data = cacheLines[index].getData();
        } else {
            handleCacheMiss(address, -1, 0); // value is junk
        }
        return data;
    }

    public void cacheWrite(int address, int value) { // TODO: write through
        index = address % numberOfLines;
        tag = address / numberOfLines;

        if (tag == cacheLines[index].getTag()) {
            cacheLines[index].setData(value);
            Simulator.getEventQueue()
                    .addEvent(new MemoryWriteEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency, this,
                            containingProcessor.getMainMemory(), address, value));
        } else {
            handleCacheMiss(address, value, 1);
        }
    }

    public void handleCacheMiss(int address, int value, int eFlag) {

        if (eFlag == 0) { // cacheRead miss
            Simulator.getEventQueue().addEvent(
                    new MemoryReadEvent(
                            Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                            this,
                            containingProcessor.getMainMemory(),
                            address));
        } else if (eFlag == 1) { // cacheWrite miss
            // TODO
            Simulator.getEventQueue()
                    .addEvent(new MemoryWriteEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency, this,
                            containingProcessor.getMainMemory(), address, value));
        }
    }

    public boolean isCacheHit(int address) {
        index = address % numberOfLines;
        tag = address / numberOfLines;

        if (tag == cacheLines[index].getTag()) {
            return true;
        } else {
            return false;
        }
    }

    public void handleResponse(int address, int value) {
        index = address % numberOfLines;
        tag = address / numberOfLines;

        cacheLines[index].setData(value);
        cacheLines[index].setTag(tag);
    }

    @Override
    public void handleEvent(Event e) {

        if (e.getEventType() == EventType.MemoryRead) {
            MemoryReadEvent event = (MemoryReadEvent) e;

            if (isCacheHit(event.getAddressToReadFrom())) {
                Simulator.getEventQueue().addEvent(
                        new MemoryResponseEvent(
                                Clock.getCurrentTime(),
                                this,
                                event.getRequestingElement(),
                                cacheRead(event.getAddressToReadFrom()), event.getAddressToReadFrom()));
                System.out.println("Cache Hit: Memory Resoponse event added from L1 for addre " + event.getAddressToReadFrom());
            } else {
                cacheRead(event.getAddressToReadFrom());
                System.out.println("Cache miss: Memory Read event send to Main Memory from L1 for addre " + event.getAddressToReadFrom());
            }
        }

        else if (e.getEventType() == EventType.MemoryWrite) {
            MemoryWriteEvent event = (MemoryWriteEvent) e;

            if (isCacheHit(event.getAddressToWriteTo())) {
                cacheWrite(event.getAddressToWriteTo(), event.getValue());
                // Simulator.getEventQueue().addEvent(
                //         new MemoryResponseEvent(Clock.getCurrentTime(), this, event.getRequestingElement(),
                //                 event.getValue(), event.getAddressToWriteTo()));
                System.out.println("Cache Hit: Memory write event added from L1 for addre " + event.getAddressToWriteTo());
            } else {
                cacheWrite(event.getAddressToWriteTo(), event.getValue());
                System.out.println("Cache miss: Memory Request event send to Main Memory from L1 for addre " + Clock.getCurrentTime());
            }

            System.out.println("Memory Response event: Memory Write fired " + event.getAddressToWriteTo());
        } else if (e.getEventType() == Event.EventType.MemoryResponse) {

            MemoryResponseEvent event = (MemoryResponseEvent) e;
            handleResponse(event.getAddressToReadFrom(), event.getValue());
            if (cacheType == 0) {
                Simulator.getEventQueue().addEvent(new MemoryResponseEvent(event.getEventTime(), this,
                        containingProcessor.getIFUnit(), event.getValue(), event.getAddressToReadFrom()));
                        System.out.println("From Cache: Mem Write event: addre: " + event.getAddressToReadFrom());
            } // ?

            else if (cacheType == 1) {
                Simulator.getEventQueue().addEvent(new MemoryResponseEvent(event.getEventTime(), this,
                        containingProcessor.getMAUnit(), event.getValue(), event.getAddressToReadFrom()));
                        System.out.println("From Cache: Mem Res event: addre: " + event.getAddressToReadFrom());
            } // ?
        }
        // else if (e.getEventType() == Event.EventType.MemoryWrite) TO IMPLEMENT FOR MA
    }
}
