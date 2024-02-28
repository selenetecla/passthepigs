import java.util.ArrayList;

class Player {
    private String name;
    private String strategy;

    public Player(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getStrategy() {
        return strategy;
    }
}

public class PassThePigs {
    public static void main(String[] args) {
        System.out.println("Let's play Pass the Pigs!");
    }
    private static int roll() {
        double rollProbability = (double) (Math.random() * 101);
        if(rollProbability < 34.90) {
            return 0; //dot
        } else if (rollProbability < 65.10) {
            return 0; // no dot
        } else if (rollProbability < 87.50) {
            return 5; //razorback
        } else if (rollProbability < 96.30) {
            return 5; //trotter
        } else if (rollProbability < 99.30) {
            return 10; //snouter
        } else {
            return 15; // leaning jowsler
        }
    }
}
// public class PassThePigs {
//     public void oink() {
//         System.out.println("oink");
//     }
// }