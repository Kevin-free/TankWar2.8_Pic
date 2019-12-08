package com.kevin.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * 爆炸
 * @author Kevin
 *
 */
public class Explode {
	TankClient tc;
	int x,y;
	private boolean live = true;
	private static boolean init = false;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();//调用第三方类Toolkit
	private static Image[] imgs = {
		//反射机制：Explode类方法Class调用getClassLoader()调用getResource
		tk.createImage(Explode.class.getClassLoader().getResource("images/0.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/1.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/2.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/3.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/4.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/5.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/6.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/7.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/8.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/9.gif"))
	};
	
	int step = 0;
	
	Explode(int x,int y,TankClient tc){
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!init) {
			for (int i = 0; i < imgs.length; i++) {
				g.drawImage(imgs[i], -100, -100, null);
			}
			init = true;
		}
		
		if(!live) {
			tc.explodes.remove(this);			
			return;
		}
		if(step == imgs.length) {
			live = false;
			step = 0;
			return;
		}
		g.drawImage(imgs[step], x, y, null);
		step++;
	}
}
