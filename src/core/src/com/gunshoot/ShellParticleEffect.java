package com.gunshoot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ShellParticleEffect extends ParticleEffect {
	
	private final TextureRegion PARTICLE_REGION;

	public ShellParticleEffect(Vector2 emitPos, Texture shotgunTexture) {
		super(emitPos);
		PARTICLE_REGION = new TextureRegion(shotgunTexture, 23, 10, 3, 2);
	}

	@Override
	public void play() {
		super.play();
		
		ShellParticle p = new ShellParticle(PARTICLE_REGION);
		
		p.generation = 0;
		
		p.position = emitPosition.cpy();
		p.rotation = 0f;
		p.direction = new Vector2(-1f, 0f);

		p.gravity = 4f;
		
		p.size = 1f;
		p.speed = 80f + 50f * (float)Math.random();
		p.angularSpeed = 0f;
		p.rotationSpeed = -500f - 500f * (float)Math.random();
		
		p.speedDecay = 0f;
		p.angularSpeedDecay = 0f;
		p.rotationSpeedDecay = 0f;
		p.sizeDecay = 0f;
		
		particles.add(p);
	}
}
