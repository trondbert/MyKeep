
class Averager {
    private int total = 0;
    private int count = 0;

    public double average() {
        return (double) total / count;
    }

    public void accept(int i) { total += i; count++; }

    public void combine(Averager other) {
        total += other.total;
        count += other.count;
    }

}