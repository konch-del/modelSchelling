public class Section {

    private boolean happy;
    private Colors color;

    public Section(Colors color) {
        this.color = color;
    }

    public Section(boolean happy, Colors color) {
        new Section(color);
        this.happy = happy;
    }

    public boolean isHappy() {
        return happy;
    }

    public void setHappy(boolean happy) {
        this.happy = happy;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color.toString();
    }
}
