package IValue;

public class VMCell implements	IValue	{
    IValue v;
    public VMCell(IValue v0)	{
        v =	v0;	}
    public IValue get()	{
        return	v;
    }
    public void set(IValue	v0)	{
        v =	v0;
    }
}
