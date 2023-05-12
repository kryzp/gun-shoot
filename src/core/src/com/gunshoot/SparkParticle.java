package com.gunshoot;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SparkParticle extends Particle {
	public SparkParticle(TextureRegion region) {
		super();
		this.sprite = new Sprite(region);
		this.sprite.setScale(size);
		this.spriteRotation = -90f;
	}
}
