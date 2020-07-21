import java.util.Random;
import java.util.Scanner;

public class WorldMaker
{
	static int x = 200;
	static boolean[][] world = new boolean[x][2 * x];
	
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter island seed: ");
		int s = sc.nextInt();
		System.out.print("Enter horizontal land stickiness: ");
		int h = sc.nextInt();
		System.out.print("Enter vertical land stickiness: ");
		int v = sc.nextInt();
		System.out.print("Enter diagonal land stickiness: ");
		int d = sc.nextInt();
		for (int i = 1; i < x - 1; i++) for (int j = 0; j < 2 * x; j++) world[i][j] = getResult(s);
		
		for (int i = x - 1; i >= 0; i--) for (int j = x * 2 - 1; j >= 0; j--) gen(i, j, h, v, d);
		for (int i = 0; i < x; i++) for (int j = 0; j < x * 2; j++) gen(i, j, h, v, d);
		for (int i = x - 1; i >= 0; i--) for (int j = 0; j < x * 2; j++) gen(i, j, h, v, d);
		for (int i = 0; i < x; i++) for (int j = x * 2 - 1; j >= 0; j--) gen(i, j, h, v, d);
		
		for (int i = 0; i < x; i++)
		{
			for (int j = 0; j < 2 * x; j++)
			{
				if (world[i][j]) System.out.print("0");
				else System.out.print(" ");
			}
			System.out.println();
		}
		sc.close();
	}
	
	static boolean getResult(int chance)
	{
		Random r = new Random();
		if (r.nextInt(1000) < chance) return true;
		return false;
	}
	
	static void gen(int i, int j, int h, int v, int d)
	{
		int hor = 0;
		int ver = 0;
		int dia = 0;
		int jprev = j - 1;
		int jnext = j + 1;
		if (j == 0) jprev = x * 2 - 1;
		else if (j == x * 2 - 1) jnext = 0;
		
		if (i == 0)
		{
			if (world[i][jprev]) hor += 1;
			if (world[i][jnext]) hor += 1;
			if (world[i + 1][j]) ver += 1;
			if (world[i + 1][jprev]) dia += 1;
			if (world[i + 1][(jnext) % (x * 2)]) dia += 1;
			if (getResult(hor * h * 7 + ver * v * 2 + dia * d * 2)) world[i][j] = true;
		}
		else if (i == x - 1)
		{
			if (world[i][jprev]) hor += 1;
			if (world[i][jnext]) hor += 1;
			if (world[i - 1][j]) ver += 1;
			if (world[i - 1][jprev]) dia += 1;
			if (world[i - 1][(jnext) % (x * 2)]) dia += 1;
			if (getResult(hor * h * 7 + ver * v * 2 + dia * d * 2)) world[i][j] = true;
		}
		else if (i * 8 <= x || (x - i) * 8 <= x)
		{
			if (world[i][jprev]) hor += 1;
			if (world[i][jnext]) hor += 1;
			if (world[i + 1][j]) ver += 1;
			if (world[i - 1][j]) ver += 1;
			if (world[i + 1][jprev]) dia += 1;
			if (world[i + 1][jnext]) dia += 1;
			if (world[i - 1][jprev]) dia += 1;
			if (world[i - 1][jnext]) dia += 1;
			if (getResult(hor * h * 3 + ver * v * 2 + dia * d * 2)) world[i][j] = true;
		}
		else
		{
			if (world[i][jprev]) hor += 1;
			if (world[i][jnext]) hor += 1;
			if (world[i + 1][j]) ver += 1;
			if (world[i - 1][j]) ver += 1;
			if (world[i + 1][jprev]) dia += 1;
			if (world[i + 1][jnext]) dia += 1;
			if (world[i - 1][jprev]) dia += 1;
			if (world[i - 1][jnext]) dia += 1;
			if (getResult(hor * h * 3 + ver * v * 3 + dia * d * 2)) world[i][j] = true;
		}
	}
}