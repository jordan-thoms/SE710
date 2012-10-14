package nz.net.thoms.HttpServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

public class SplitBlocks {
	static int MAX_BLOCK = 4096;
	static int MIN_BLOCK  = 128;
	
	public static List<Block> splitBlocks(byte[] file) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		int rabin = 0;
		int blockStart = 0;
		for (int i =0; i< file.length; i++) {
			rabin = Rabin.calculateRabin(file, rabin, i-1);
			if ((i - blockStart) < MIN_BLOCK) {
				continue;
			}
			if ((rabin % 2048) == 0) {
//				System.out.println("Normal block from " + blockStart + " to "+ i);				
				addBlock(file, blocks, blockStart, i);
				blockStart = i;
			}
			
			if ((i - blockStart) > MAX_BLOCK) {
//				System.out.println("Length-bound block from " + blockStart + " to "+ i);				
				addBlock(file, blocks, blockStart, i);
				blockStart = i;			
			}
		}
		
		// pick up the end
		if (blockStart != file.length +1) {
//			System.out.println("Final block from " + blockStart + " to "+ (file.length));
			addBlock(file, blocks, blockStart, file.length);
		}
		return blocks;
	}
	
	private static void addBlock(byte[] file, List<Block> blocks, int start, int end) {
		byte[] range = Arrays.copyOfRange(file, start, end);
		String digest = DigestUtils.md5Hex(range);
		blocks.add(new Block(range, digest));
	}
	
	public static class Block {
		byte[] block;
		String md5Digest;
		
		public Block(byte[] block, String md5Digest) {
			this.block = block;
			this.md5Digest = md5Digest;
		}
	}
}
