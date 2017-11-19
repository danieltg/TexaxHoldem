package GameDescriptor;

public class Blindes {

    private boolean fixed;
    private int big;
    private int small;
    private int additions;
    private int maxTotalRounds;

    public Blindes(int big, int small,boolean fixed, int additions, int maxTotalRounds ) {
        this.big = big;
        this.small = small;
        this.fixed = fixed;
        this.additions=additions;
        this.maxTotalRounds=maxTotalRounds;
    }

    //Getter
    public boolean isFixed() { return fixed; }

    public int getBig() { return big; }

    public int getSmall() { return small; }

    public int getAdditions() { return additions; }

    public int getMaxTotalRounds() { return maxTotalRounds; }

    //Setter
    public void setFixed(boolean fixed) { this.fixed = fixed; }

    public void setBig(int big) { this.big = big; }

    public void setSmall(int small) { this.small = small; }

    public void setAdditions(int additions) { this.additions = additions; }

    public void setMaxTotalRounds(int maxTotalRounds) { this.maxTotalRounds = maxTotalRounds; }

    @Override
    public String toString() {
        return "Blindes{" +
                "fixed=" + fixed +
                ", big=" + big +
                ", small=" + small +
                ", additions=" + additions +
                ", maxTotalRounds=" + maxTotalRounds +
                '}';
    }

    public static boolean validateBlindes(Blindes b)
    {
        if (b.getSmall()<0 )
            throw new IllegalArgumentException("Small <"+b.getSmall()+"> cannot be negative");

        if (b.getBig()<0)
            throw new IllegalArgumentException("Big <"+b.getBig()+"> cannot be negative");

        if(b.getAdditions()<0)
            throw new IllegalArgumentException("Additions <"+b.getAdditions()+"> cannot be negative");

        if(b.getMaxTotalRounds()<0)
            throw new IllegalArgumentException("Max Total Rounds <"+b.getMaxTotalRounds()+"> cannot be negative");

        if(b.getSmall()>=b.getBig())
            throw new IllegalArgumentException("Big <"+b.getBig()+"> cannot be smaller then Small <"+b.getSmall()+">");

        return true;
    }
}
