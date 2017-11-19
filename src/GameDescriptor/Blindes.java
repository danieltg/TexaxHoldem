package GameDescriptor;

import Exceptions.BlindesException;

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

    public static boolean validateBlindes(Blindes b) throws BlindesException {
        if (b.getSmall()<0 )
            throw new BlindesException(BlindesException.NEGATIVE_SMALL);

        if (b.getBig()<0)
            throw new BlindesException(BlindesException.NEGATIVE_BIG);

        if(b.getAdditions()<0)
            throw new BlindesException(BlindesException.NEGATIVE_ADDITIONS);

        if(b.getMaxTotalRounds()<0)
            throw new BlindesException(BlindesException.NEGATIVE_MAX_TOTAL_ROUNDS);

        if(b.getSmall()>=b.getBig())
            throw new BlindesException(BlindesException.SMALL_BIGGER_THEN_SMALL);

        return true;
    }
}
