package com.kevin.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 设置坦克
 * @author Kevin
 *
 */
public class Tank {
	public static final int XSPEED = 5; //坦克X方向速度
	public static final int YSPEED = 5; //坦克Y方向速度

	public static final int WIDTH = 30; //坦克宽度
	public static final int HEIGHT = 30;//坦克高度

	TankClient tc;
	
	private int x, y;
	private int oldX,oldY;
	private int life = 100;
	
	private BloodBar bb = new BloodBar();

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	private static Random r = new Random();
	
	private int step = r.nextInt(12)+3;//定义敌方坦克向某一方向移动随机步数:nextInt(12)为[0,11],最小移动0+3，最大移动11+3
	
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	private boolean good;
	
	public boolean isGood() {
		return good;
	}

	private boolean bL=false, bU=false, bR=false, bD=false;
	private Direction dir = Direction.STOP;	
	private Direction ptDir = Direction.D;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();//调用第三方类Toolkit
	private static Image[] tankImgs = null;
	private static Map<String, Image> imgs= new HashMap<String, Image>();
	
	//static静态放置代码区：程序加载运行此代码
	static {
		tankImgs = new Image[] {
			//反射机制：Explode类方法Class调用getClassLoader()调用getResource
			tk.createImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
			tk.createImage(Tank.class.getClassLoader().getResource("images/tankLD.gif"))
		};
		imgs.put("L", tankImgs[0]);
		imgs.put("LU", tankImgs[1]);
		imgs.put("U", tankImgs[2]);
		imgs.put("RU", tankImgs[3]);
		imgs.put("R", tankImgs[4]);
		imgs.put("RD", tankImgs[5]);
		imgs.put("D", tankImgs[6]);
		imgs.put("LD", tankImgs[7]);
	}

	
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}
	
	public Tank(int x,int y,boolean good, Direction dir,TankClient tc) {
		this(x, y,good);
		this.dir = dir;
		this.tc = tc;
	}
	
	/**
	 * 绘制
	 * @param g
	 */
	public void draw(Graphics g) {
		if(!live)	{
			if(!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		
		Color c = g.getColor();
		if(good)	g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		
		if(isGood()) bb.draw(g);
	
		switch (ptDir) {
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
	
	/**
	 * 移动
	 */
	public void move() {
		
		this.oldX = x;
		this.oldY = y;
		
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
		case STOP:
			break;
		}
		
		if(this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}
		
		//处理坦克出界问题
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		
		if(!good) {
			Direction [] dirs = Direction.values();
			if(step == 0) {
				step = r.nextInt(12)+3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step --;
			if(r.nextInt(40)>38) fire();//敌方坦克随机开炮
		}
		
	}
	
	/**
	 * 键盘按下事件
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_F2:
			if(!this.live) {
				this.live = true;
				this.life = 100;
			}
			break;
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_1:
			superFire();
			break;
		/*case KeyEvent.VK_2:
			sSuperfire();
			break;*/
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}

		locateDirection();
	}
	
	void locateDirection() {
		if (bL && !bU && !bR && !bD) dir = Direction.L;
		else if (bL && bU && !bR && !bD) dir = Direction.LU;
		else if (!bL && bU && !bR && !bD) dir = Direction.U;
		else if (!bL && bU && bR && !bD) dir = Direction.RU;
		else if (!bL && !bU && bR && !bD) dir = Direction.R;
		else if (!bL && !bU && bR && bD) dir = Direction.RD;
		else if (!bL && !bU && !bR && bD) dir = Direction.D;
		else if (bL && !bU && !bR && bD) dir = Direction.LD;
		else if (!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}

	/**
	 * 键盘抬起事件
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		}

		locateDirection();
		
	}
	
	/**
	 * 开火（发射子弹）
	 * @return 子弹
	 */
	public Missile fire() {
		if(!live) return null;
		int x = this.x + 50/2 - Missile.WIDTH/2;
		int y = this.y + 55/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y,good, ptDir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	/**
	 * 带方向参数的开火
	 * @param dir 为了superFire设置方向
	 * @return 子弹
	 */
	public Missile fire(Direction dir) {
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y,good, dir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	/**
	 * 借助第三方类Rectangle，判断碰撞检测
	 * @return
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	/**
	 * 停留方法:用于撞上时回到上一步
	 */
	private void stay() {
		x = oldX;
		y = oldY;
	}
	
	/**
	 * 判断是否撞墙
	 * @param w 被撞的墙
	 * @return 撞上了返回true，否则false
	 */
	public boolean collidesWithWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.stay();
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否撞坦克
	 * 
	 * @param tanks 敌方坦克
	 * @return
	 */
	public boolean collidesWithTanks(List<Tank> tanks) {
		for(int i = 0; i < tanks.size(); i++) {
			Tank t =tanks.get(i);
			if(this != t) {
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 超级炮弹
	 */
	public void superFire() {
		Direction [] dirs = Direction.values();
		for(int i = 0; i<8; i++) {
			fire(dirs[i]);
		}
	}

	/*public Missile sSuperfire() {
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m2 = new Missile(x, y,good, ptDir,this.tc);
		m2.WIDTH = 50;
		m2.HEIGHT = 50;
		tc.missiles.add(m2);
		return m2;
	}*/
	
	/**
	 * 定义坦克内部类：血条
	 * @author Kevin
	 *
	 */
	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, WIDTH, 10);
			g.fillRect(x, y-10, WIDTH*life/100, 10);
			g.setColor(c);
		}
	}

	/**
	 * 吃血
	 * @param b 血条
	 * @return
	 */
	public boolean eat(Blood b) {
		if(this.live && this.getRect().intersects(b.getRect())) {
			this.life = 100;
			b.setLive(false);
			return true;
		}
		return false;
	}


}

