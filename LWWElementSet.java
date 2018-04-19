package Jugs.New;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LWWElementSet
{
    public String element;
    public Map<String, Long> lwwElementSet;
    public Map<String, Long> mergeSet;
    public Map<String, Long> addSet;
    public Map<String, Long> removeSet;


    public LWWElementSet()
    {
        this.element = null;
        this.lwwElementSet = new HashMap<>();
        this.mergeSet = new HashMap<>();
        this.addSet = new HashMap<>();
        this.removeSet = new HashMap<>();
    }

    public LWWElementSet(String element, Map<String, Long> lwwElementSet, Map<String, Integer> mergeSet, Map<String, Integer> addSet, Map<String, Integer> removeSet)
    {
        this.element = element;
        this.lwwElementSet = new HashMap<>();
        this.mergeSet = new HashMap<>();
        this.addSet = new HashMap<>();
        this.removeSet = new HashMap<>();
    }

    public void addElement(String element)
    {
        Long timeStamp = System.currentTimeMillis()+lwwElementSet.size();
        this.lwwElementSet.put(element, timeStamp);
        this.addSet.put(element, timeStamp);
    }

    public void removeElement(String element)
    {
        Long timeStamp = System.currentTimeMillis()+lwwElementSet.size();
        if (lookupElement(element))
        {
            this.lwwElementSet.remove(element);
            this.removeSet.put(element, timeStamp+removeSet.size());
        }
        else
            System.err.println("Invalid!");
    }

    private Boolean validRemove(String element)
    {
        Boolean valid = false;
//        if (this.addSet.containsKey(element))
//        {
//            if (this.addSet.get(element) > this.timestamp)
//                valid = true;
//        }
        return valid;
    }

    private boolean lookupElement(String e)
    {
        Boolean isMember = false;
        if (this.addSet.containsKey(e) && (!this.removeSet.containsKey(e)))
        {
            isMember = true;
        }
        else if (this.removeSet.containsKey(e))
        {
            if (this.removeSet.get(e) < this.addSet.get(e))
            {
                isMember = true;
            }
        }
        return isMember;
    }

    public Map getLWWElementSet(Map addSet, Map removeSet)
    {
        if (!addSet.entrySet().contains(removeSet.entrySet()))
            this.lwwElementSet.putAll(addSet);

        return this.lwwElementSet;
    }

    public Map getMergeSet()
    {
        Map<String, Long> mergeSet = new HashMap<>();
        mergeSet.putAll(this.addSet);
        mergeSet.putAll(this.removeSet);
        return mergeSet;
    }

    public static void main(String[] args)
    {
        LWWElementSet ls = new LWWElementSet();
        ls.addElement("a");
        ls.addElement("b");
        ls.addElement("c");
        ls.removeElement("c");
        ls.removeElement("x");
        ls.removeElement("y");

        ls.lookupElement("c");

        System.out.println();
    }




}
