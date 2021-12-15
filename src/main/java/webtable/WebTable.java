package webtable;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class WebTable
{
    WebDriver driver;
    int row_count=0;
    int column_count=0;
    Object cells[][]=null;
    @BeforeTest
    public void chrome()
    {
        WebDriverManager.chromedriver().setup();
        driver= new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://www.techlistic.com/p/demo-selenium-practice.html");

    }
    @DataProvider(name="cells")
    public Object[][] cells(){
        cells=getCells();
        return cells;

    }
    public Object[][] getCells()
    {
        row_count=driver.findElements(By.xpath("//tbody/tr")).size();
        //System.out.println(row_count);
        column_count=driver.findElements(By.xpath("//tbody/tr[1]/child::*")).size();
        //System.out.println(column_count);
        Object data[][]=new Object[row_count+1][column_count];
        for (int headIndex=1;headIndex<column_count;headIndex++){
            data[0][headIndex-1]=driver.findElement(By.xpath("//thead/tr/th["+headIndex+"]")).getText();
            // System.out.println(data[0][headIndex-1]);
        }

        for (int rowIndex=1;rowIndex<=row_count;rowIndex++){
            for (int cellIndex=0;cellIndex<column_count;cellIndex++){
                if(cellIndex==0){
                    data[rowIndex][cellIndex]=driver.findElement(By.xpath("//tbody/tr["+rowIndex+"]/th")).getText();
                    continue;
                }
                data[rowIndex][cellIndex]=driver.findElement(By.xpath("//tbody/tr["+rowIndex+"]/td["+cellIndex+"]")).getText();
                //System.out.println(data[rowIndex][cellIndex]);
            }
            System.out.println();
        }
        return data;
    }

    @Test(priority = 0)
    public void countStructure(){
        List<WebElement> structure = driver.findElements(By.xpath("//tbody/tr/th"));
        WebElement total=driver.findElement(By.xpath("//td[contains(text(),'4 buildings')]"));
        String str= total.getText();
        String num= String.valueOf(str.charAt(0));
        // System.out.println(num);
        if (structure.size() == Integer.parseInt(num)){
            System.out.println("number of structure column = "+structure.size());
            System.out.println("Total = "+num);
        }

    }

    @Test(priority = 1,dataProvider = "cells")
    public void printData(String Structure,String Country ,String City,String Height,String Built,String Rank,String blank){
        System.out.println(Structure+"   "+Country+"   "+City+"   "+Height+"   "+Built+"   "+Rank+"   "+blank);

    }

    @Test(priority = 2)
    public void compareHeight()
    {
        List<WebElement> heightColumn= driver.findElements(By.xpath("//tbody/tr/td[3]"));
        String string[]=new String[heightColumn.size()];
        //int height=cells[][3]
        //System.out.println(heightColumn.size());
        for (int i=1;i<=string.length;i++)
        {

                string[i-1] = driver.findElement(By.xpath("//tbody/tr["+i+"]/td[3]")) .getText();
                 // System.out.println(cells[i][3]);
                 //System.out.println(string[i-1]);
                if (cells[i][3].equals(string[i-1]))
                {
                    System.out.println("expected height = " + cells[i][3] + " actual Height = " + string[i-1]);


                 }
        }





    }
    @Test(priority = 3)
    public void checkLastColumn(){
        int size=driver.findElements(By.xpath("//tfoot/tr/child::*")).size();
        if(size==2){
            System.out.println("Verified that 6th row (Last Row) has only two columns.");
        }
        else
        {
            System.out.println("6th row not having two columns");
        }
    }

    @AfterTest
    public void close(){
        driver.quit();
    }

}