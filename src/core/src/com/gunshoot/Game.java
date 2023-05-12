package com.gunshoot;

/**
 * Yes, I know how garbage this all is.
 * I just wanted to make a funny gun shoot, I don't
 * really care about how clean or elegans or wasteful the code is.
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {

	public static final int SCALE = 8;

	private static final float BLUR_AMOUNT = 10f;
	private static final int BLUR_SCALE_FACTOR = 4;
	
	private SpriteBatch batch;
	private Gun currentGun;
	private FrameBuffer renderTarget;
	private FrameBuffer bloomTarget0;
	private FrameBuffer bloomTarget1;
	private FrameBuffer bloomTarget2;
	private ShaderProgram blurShaderH;
	private ShaderProgram blurShaderV;
	private ShaderProgram postProcessShader;
	private ShaderProgram finalShader;
	private FrameBuffer pixelPerfectTarget;
	private float globalBrightness = 1f;
	
	public static OrthographicCamera camera = null;

	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(true, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);
		currentGun = new Shotgun();
		blurShaderH = new ShaderProgram(Gdx.files.internal("blur_hori_vert.glsl").readString(), Gdx.files.internal("blur_hori_frag.glsl").readString());
		blurShaderV = new ShaderProgram(Gdx.files.internal("blur_vert_vert.glsl").readString(), Gdx.files.internal("blur_vert_frag.glsl").readString());
		postProcessShader = new ShaderProgram(Gdx.files.internal("post_proc_vert.glsl").readString(), Gdx.files.internal("post_proc_frag.glsl").readString());
		finalShader = new ShaderProgram(Gdx.files.internal("final_vert.glsl").readString(), Gdx.files.internal("final_frag.glsl").readString());
		renderTarget = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		bloomTarget0 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth() / BLUR_SCALE_FACTOR, Gdx.graphics.getHeight() / BLUR_SCALE_FACTOR, true);
		bloomTarget1 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth() / BLUR_SCALE_FACTOR, Gdx.graphics.getHeight() / BLUR_SCALE_FACTOR, true);
		bloomTarget2 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth() / BLUR_SCALE_FACTOR, Gdx.graphics.getHeight() / BLUR_SCALE_FACTOR, true);
		pixelPerfectTarget = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE, true);
	}

	@Override
	public void render() {

		globalBrightness = MathUtils.lerp(globalBrightness, 1f, 0.1f);
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			if (currentGun.shoot()) {
				globalBrightness = 0.5f;
			}
		}

		camera.position.x = MathUtils.lerp(camera.position.x, camera.viewportWidth / 2f, 0.2f);
		camera.position.y = MathUtils.lerp(camera.position.y, camera.viewportHeight / 2f, 0.2f);

		camera.update();
		currentGun.update(Gdx.graphics.getDeltaTime());
		
		batch.setProjectionMatrix(camera.combined);

		renderTarget.begin();
		batch.begin();
		ScreenUtils.clear(0.11f, 0.11f, 0.14f, 1.0f);
		currentGun.render(batch);
		batch.end();
		renderTarget.end();
		
		bloomTarget0.begin();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		ScreenUtils.clear(0f, 0f, 0f, 1f);
		currentGun.renderBloom(batch);
		batch.end();
		bloomTarget0.end();
		
		batch.getProjectionMatrix().setToOrtho2D(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		bloomTarget1.begin();
		batch.begin();
		batch.setShader(blurShaderH);
		blurShaderH.setUniformf(blurShaderH.getUniformLocation("u_texSize"), new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		blurShaderH.setUniformf(blurShaderH.getUniformLocation("u_blurAmount"), BLUR_AMOUNT);
		TextureRegion r11 = new TextureRegion(bloomTarget0.getColorBufferTexture());
		r11.flip(false, true);
		batch.draw(r11, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setShader(null);
		batch.end();
		bloomTarget1.end();
		
		bloomTarget2.begin();
		batch.begin();
		batch.setShader(blurShaderV);
		blurShaderV.setUniformf(blurShaderV.getUniformLocation("u_texSize"), new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		blurShaderV.setUniformf(blurShaderV.getUniformLocation("u_blurAmount"), BLUR_AMOUNT);
		TextureRegion r22 = new TextureRegion(bloomTarget1.getColorBufferTexture());
		r22.flip(false, true);
		batch.draw(r22, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setShader(null);
		batch.end();
		bloomTarget2.end();

		pixelPerfectTarget.begin();
		batch.begin();
		batch.setShader(postProcessShader);
		postProcessShader.setUniformf(postProcessShader.getUniformLocation("u_globalBrightness"), globalBrightness);
		bloomTarget2.getColorBufferTexture().bind(1);
		postProcessShader.setUniformi(postProcessShader.getUniformLocation("u_bloom"), 1);
		renderTarget.getColorBufferTexture().bind(0);
		TextureRegion rr = new TextureRegion(renderTarget.getColorBufferTexture());
		rr.flip(false, true);
		batch.draw(rr, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setShader(null);
		batch.end();
		pixelPerfectTarget.end();
		
		batch.begin();
		batch.setShader(finalShader);
		finalShader.setUniformf(finalShader.getUniformLocation("u_texSize"), new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		TextureRegion r33 = new TextureRegion(pixelPerfectTarget.getColorBufferTexture());
		r33.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		r33.flip(false, true);
		batch.draw(r33, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setShader(null);
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		currentGun.dispose();
	}
}
