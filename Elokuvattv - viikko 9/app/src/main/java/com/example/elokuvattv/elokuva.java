package com.example.elokuvattv;

public class elokuva {
    private String title, line2, time;
    private int teatteriid;

    public elokuva(String newtitle, String newline2, String time, int Teatteri) {
        this.title = newtitle;
        this.line2 = newline2;
        this.time = time;
        this.teatteriid = Teatteri;
    }

    public String getTitle() {
        return title;
    }

    public String getLine2() {
        return line2;
    }

 //   public String getTime() {
  //      return time;
 //   }

    public int getTeatteriId() {
        return teatteriid;
    }

    public int getTime () {
        String[] temp = time.split(":");
        return ((Integer.parseInt(temp[0]) * 60) + Integer.parseInt(temp[1]));
    }
}
