package com.kevin.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 设置子弹
 * @author Kevin
 *
 */
public class Missile {
	public static final int XSPEED = 20;
	public static final int YSPEED = 20;

	public static int WIDTH = 10;
	public static int HEIGHT = 10;

	int x,y;
	Direction dir;
	
	private boolean live = true;
	
	private boolean good;
	
	private TankClient tc;
	
	public boolean isLive() {
		return live;
	}

	private static Toolkit tk = Toolkit.getDefaultToolkit();//调用第三方类Toolkit
	private static Image[] missileImgs = null;
	private static Map<String, Image> imgs= new HashMap<String, Image>();

	//static静态放置代码区：程序加载运行此代码
	static {
		missileImgs = new Image[] {
			//反射机制：Explode类方法Class调用getClassLoader()调用getResource
			tk.createImage(Tank.class.getClassLoader().getResource("images/missileL.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/missileLU.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/missileD.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/missileRU.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/missileR.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/missileRD.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/missileU.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/missileLD.gif"))
		};
		imgs.put("L", missileImgs[0]);
		imgs.put("LU", missileImgs[1]);
		imgs.put("U", missileImgs[2]);
		imgs.put("RU", missileImgs[3]);
		imgs.put("R", missileImgs[4]);
		imgs.put("RD", missileImgs[5]);
		imgs.put("D", missileImgs[6]);
		imgs.put("LD", missileImgs[7]);

	}

	
	public Missile(int x, int y, Direction dir) {
		super();
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile (int x, int y,boolean good, Direction dir, TankClient tc) {
		this(x, y,dir);
		this.good = good;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			tc.missiles.remove(this);
			return;			
		}
		switch (dir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		}
		move();
	}

	public void move() {
		switch (dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		}
	
		if(x<0 || y<0 || x>TankClient.GAME_WIDTH || y>TankClient.GAME_HEIGHT) {
			live = false;
			
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	//实现子弹是否打中坦克
	public boolean hitTank(Tank t) {
		if(this.getRect().intersects(t.getRect()) && t.isLive() && this.good!=t.isGood()) {
			if(t.isGood()) {
				t.setLife(t.getLife()-25);
				if(t.getLife()<=0) t.setLive(false);
			}else {
				t.setLive(false);
			}
			this.live = false;
			Explode e = new Explode(x,y,tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}

	public boolean hitTanks(List<Tank> tanks) {
		for(int i = 0; i<tanks.size();i++) {
			if(hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}
	
	
	
}

