package processor.memorysystem;

public class CacheLine {

    int Tag;
    int Data;

    public CacheLine()
    {
        Tag = -1;
        Data = -1;
    }
    
    public void setTag(int tag)
    {
        Tag = tag;
    }
    
    public void setData(int data)
    {
        Data = data;
    }

    public int getTag()
    {
        return Tag;
    }
    
    public int getData()
    {
        return Data;
    }
}
