import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        SchellingsModel schellingsModel = new SchellingsModel(100, 45, 45, 2);
        schellingsModel.simulate(100, 10);
    }
}
