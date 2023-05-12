package com.gunshoot;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class Particle {

	private float gravityVelocity = 0f;
	
	public int generation = 0;
	
	public Sprite sprite;
	public float spriteRotation;
	
	public Vector2 position;
	public Vector2 direction;
	public float rotation;
	public float gravity;

	public float size;
	public float speed;
	public float angularSpeed;
	public float rotationSpeed;

	public float sizeDecay;
	public float speedDecay;
	public float angularSpeedDecay;
	public float rotationSpeedDecay;
	
	public Particle() {
		this.spriteRotation = 0f;
		
		this.position = Vector2.Zero.cpy();
		this.direction = Vector2.Zero.cpy();
		this.rotation = 0f;
		this.gravity = 0f;

		this.size = 1f;
		this.speed = 0f;
		this.angularSpeed = 0f;
		this.rotationSpeed = 0f;

		this.speedDecay = 0f;
		this.angularSpeedDecay = 0f;
		this.rotationSpeedDecay = 0f;
		this.sizeDecay = 0f;
	}

	public void update(float dt) {
		speed = MathUtils.lerp(speed, 0f, speedDecay);
		angularSpeed = MathUtils.lerp(angularSpeed, 0f, angularSpeedDecay);
		rotationSpeed = MathUtils.lerp(rotationSpeed, 0f, rotationSpeedDecay);
		size = MathUtils.lerp(size, 0f, sizeDecay);
		position.add(direction.cpy().nor().scl(speed * dt));
		direction.rotateDeg(angularSpeed * dt);
		rotation += rotationSpeed * dt;
		gravityVelocity += gravity;
		position.y += gravityVelocity * dt;
	}

	public void render(SpriteBatch b) {
		sprite.setOriginBasedPosition(position.x, position.y);
		sprite.setScale(size);
		sprite.setRotation(direction.angleDeg() + spriteRotation + rotation);
		sprite.draw(b);
	}
}
