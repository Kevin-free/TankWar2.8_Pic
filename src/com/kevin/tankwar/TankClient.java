package com.kevin.tankwar;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * 这个类的作用是坦克游戏的主窗口
 * @author Kevin
 *
 */
public class TankClient extends Frame{

	/**
	 * 整个坦克游戏的宽度
	 */
	public static final int GAME_WIDTH = 800;//常量
	/**
	 * 整个坦克游戏的高度
	 */
	public static final int GAME_HEIGHT = 600;	
	
	Image offScreenImage = null;
	
	Blood b = new Blood();
	
	Tank mytank = new Tank(50, 50, true , Direction.STOP,this);
	
	List<Missile> missiles = new ArrayList<Missile>();	
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	
	Wall w1 = new Wall(100, 100,30,100,this);
	Wall w2 = new Wall(400, 500,200,30,this);
	
	//
	@Override
	/**
	 * 绘制坦克、子弹、墙
	 */
	public void paint(Graphics g) {
		/*
		 * 指明子弹、爆炸、坦克的数量
		 * 以及坦克的生命值
		 */
		g.drawString("missiles count = " + missiles.size(), 10, 50);
		g.drawString("explodes count = " + explodes.size(), 10, 70);
		g.drawString("tanks    count = " + tanks.size(), 10, 90);
		g.drawString("    life       = " +mytank.getLife(), 10,110);
		
		if(tanks.size() <= 0) {
			for(int i = 0; i<Integer.parseInt(PropertyMgr.getProperty("reproduceTankCount")); i++) {
				tanks.add(new Tank(100+50*(i+1),50,false,Direction.D,this));
			}			
		}
		
		for(int i = 0; i<missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(mytank);
			m.draw(g);
			m.hitWall(w1);
			m.hitWall(w2);
		}
		for(int i = 0; i<explodes.size();i++) {
			Explode e = explodes.get(i);
			e.draw(g);			
		}
		for(int i = 0; i<tanks.size();i++) {
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}
		mytank.draw(g);		
		mytank.eat(b);
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
	}
	
	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);			
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.BLACK);
		gOffScreen.fillRect(0, 0, GAME_WIDTH,GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	/**
	 * 显示坦克主窗口
	 */
	public void lunchFrame() {
		
		
		int initTankCount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
		for(int i = 0; i<initTankCount; i++) {
			tanks.add(new Tank(100+50*(i+1),50,false,Direction.D,this));
		}
		this.setLocation(200,100);
		this.setTitle("TankWar");
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setResizable(false);//设置此窗体是否可由用户调整大小。		
		this.setBackground(Color.GREEN);//设置背景色
		
		this.addKeyListener(new KeyMonitor());
		
		setVisible(true);	
		new Thread(new PaintThread()).start();
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lunchFrame();
	}

	/**
	 * 启动绘画线程
	 * @author Kevin
	 *
	 */
	private class PaintThread implements Runnable{

		@Override
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	/**
	 * 按键监听
	 * @author Kevin
	 *
	 */
	private class KeyMonitor extends KeyAdapter{

		@Override
		public void keyReleased(KeyEvent e) {
			mytank.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			mytank.keyPressed(e);
		}
		
		
	}
	
}







