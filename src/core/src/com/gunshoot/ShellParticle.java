package com.gunshoot;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ShellParticle extends Particle {
	public ShellParticle(TextureRegion region) {
		super();
		this.sprite = new Sprite(region);
		this.sprite.setScale(size);
	}
}
