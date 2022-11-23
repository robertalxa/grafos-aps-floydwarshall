import model.Graph;
import model.list.FloydWarshall;
import util.LoadData;

public class Main {
    public static void main(String[] args) {
        FloydWarshall lisGraph = LoadData.loadList("data/data.txt");
    }
}