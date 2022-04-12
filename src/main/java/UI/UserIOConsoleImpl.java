package UI;

import Exceptions.InvalidOrderException;
import java.math.BigDecimal;
import java.util.Scanner;



public class UserIOConsoleImpl implements UserIO {
    Scanner console = new Scanner(System.in);

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    @Override
    public double readDouble(String prompt) {
        boolean invalidInput = true;
        double num = 0;
        while(invalidInput)
        {
            try{
                String stringValue = this.readString(prompt);
                num = Double.parseDouble(stringValue);
                invalidInput = false;
            } catch (NumberFormatException e)
            {
                this.print("Input error. Please try again.");
            }
        }
        return num;
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        return 0;
    }

    @Override
    public float readFloat(String prompt) {
        return 0;
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        return 0;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt){

        boolean invalidInput = true;
        BigDecimal bigDecimal = new BigDecimal(0);
        while(invalidInput)
        {
            try{
                String stringValue = this.readString(prompt);
                if(stringValue.isEmpty())
                {
                    stringValue="0";
                }
                bigDecimal = new BigDecimal(stringValue);
                invalidInput = false;
            } catch (NumberFormatException e)
            {
                this.print("Input error. Please try again:");
            }
        }
        return bigDecimal;
    }

    @Override
    public BigDecimal readBigDecimalArea(String prompt){

        boolean invalidInput = true;
        BigDecimal bigDecimal = new BigDecimal(0);
        double num = 0;
        while(invalidInput)
        {
            try{
                String stringValue = this.readString(prompt);
                num = Double.parseDouble(stringValue);
                if(num < 100){throw new InvalidOrderException("");}
                bigDecimal = new BigDecimal(stringValue);
                invalidInput = false;
            } catch (NumberFormatException e)
            {
                this.print("Input error. Please try again:");
            } catch (InvalidOrderException e)
            {
                this.print("Invalid SqFt area. Minimum 100 SqFt required!. Please input again:");
            }
        }
        return bigDecimal;
    }

    @Override
    public int readInt(String prompt) {
        boolean invalidInput = true;
        int num = 0;
        while(invalidInput)
        {
            try{
                String stringValue = this.readString(prompt);
                num = Integer.parseInt(stringValue);
                invalidInput = false;
            } catch (NumberFormatException e)
            {
                this.print("Input error. Please try again.");
            }
        }
        return num;
    }

    @Override
    public char readChar(String prompt) {
        boolean invalidInput = true;
        char choice = 0;
        while(invalidInput)
        {
                String stringValue = this.readString(prompt);
                choice = stringValue.trim().toUpperCase().charAt(0);
                if(choice == 'Y' || choice == 'N')
                {
                    invalidInput = false;
                    
                } else {
                    this.print("Input error. Please try again.");
                }
        }
        return choice;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int result;
        do{
            result = readInt(prompt);
        } while (result < min || result > max);
        return result;

    }

    @Override
    public long readLong(String prompt) {
        return 0;
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        return 0;
    }

    @Override
    public String readString(String prompt) {
        this.print(prompt);

        return console.nextLine();
    }
  
}
