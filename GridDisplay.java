import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GridDisplay extends JPanel
{

	static GridSquare[][] mazeGrid = new GridSquare[19][13];
	
	public static void main(String[] args)
	{
		
		
	}
	
	public static void getMaze(GridSquare[][] mazeGridIn)
	{
		mazeGrid = mazeGridIn;
	}
		
	public void paint(Graphics g) {
		
		int currentX = 0;
		int currentY = 0;
		
		for(int k = 0; k < 13; k++)
		{
			for(int i = 0; i < 19; i++)
			{
				if(mazeGrid[i][k].isGreen)
				{
					g.setColor(Color.GREEN);
					g.drawRect (currentX, currentY, 50, 50); 
					g.fillRect(currentX, currentY, 50, 50);
					currentX += 50;
				}
				else if(mazeGrid[i][k].isRed)
				{
					g.setColor(Color.RED);
					g.drawRect (currentX, currentY, 50, 50); 
					g.fillRect(currentX, currentY, 50, 50);
					currentX += 50;
				}
				else if(mazeGrid[i][k].traversable) {
					if(i%2 == 0 && k%2 == 0) {
						g.setColor(Color.BLACK);
						g.drawRect (currentX, currentY,20,20); 
						currentX += 20;
					}
					else if(i%2 == 0 ) {
						g.setColor(Color.BLACK);
						g.drawRect (currentX, currentY,20,50); 
						currentX += 20;
					}
					else if(k%2 == 0) {
						g.setColor(Color.BLACK);
						g.drawRect (currentX, currentY,50,20); 
						currentX += 50;
					}
					else
					{
						g.setColor(Color.BLACK);
						g.drawRect (currentX, currentY,50, 50); 
						currentX += 50;
					}
					
				} else 
					{
					if(i%2 == 0 && k%2 == 0) 
					{
						g.setColor(Color.BLACK);
						g.drawRect (currentX, currentY,20,20); 
						g.fillRect(currentX, currentY, 20, 20);
						currentX += 20;
					}
					else if(i%2 == 0 ) 
					{
						g.setColor(Color.BLACK);
						g.drawRect (currentX, currentY,20,50);
						g.fillRect(currentX, currentY, 20, 50);
						currentX += 20;
					}
					else if(k%2 == 0) {
						
						g.setColor(Color.BLACK);
						g.drawRect (currentX, currentY,50,20); 
						g.fillRect(currentX, currentY, 50, 20);
						currentX += 50;
					}
					else
					{
						g.setColor(Color.BLACK);
						g.drawRect (currentX, currentY, 50, 50); 
						g.fillRect(currentX, currentY, 50, 50);
						currentX += 50;
					}
					
				}
				
				
			}
			currentX = 0;
			if(k%2 == 0)
			{
				currentY += 20;
			}
			else
			{
				currentY += 50;
			}
			
		}
		
		  
	}

}


	  
	
