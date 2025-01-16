package b100.continuousmusic;

import net.minecraft.client.sound.Channel;

/**
 * This thread to allow music to play during loading screens.
 * Background music is streamed, and needs constant supply of buffers,
 * which is done in SoundSystem.tick, however that method is not called
 * during loading screens. If the loading screen takes too long, the buffers
 * fill up, and the music stops playing.
 */
public class TickChannelThread implements Runnable {
	
	private final Channel channel;
	
	public TickChannelThread(Channel channel) {
		this.channel = channel;
		
		Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.setName("TickSound");
		thread.start();
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(100);
			}catch (Exception e) {
				e.printStackTrace();
			}
			channel.tick();
		}
	}
	
}
