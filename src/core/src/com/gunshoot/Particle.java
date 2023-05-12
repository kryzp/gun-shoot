package com.gunshoot;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Particle {

	public int generation = 0;

	public abstract void update(float dt);
	public abstract void render(SpriteBatch b);
}
