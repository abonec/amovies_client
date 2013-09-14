package com.abonec.AmoviesParser;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 9/12/13
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AmoviesFragment {
    public AmoviesEntry getEntry();
    public void updateView();
    public void setAmoviesEntry(AmoviesEntry entry);
    public void resetView();
    public boolean compatibleWith(AmoviesEntry entry);
}
