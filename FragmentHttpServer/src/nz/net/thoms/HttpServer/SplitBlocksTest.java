package nz.net.thoms.HttpServer;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nz.net.thoms.HttpServer.SplitBlocks.Block;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class SplitBlocksTest {

	
	@Test
	public void testSplitBlocks() {
		byte[] bytes = {124};
		
		List<Block> blocks = SplitBlocks.splitBlocks(bytes);
		assertEquals(1, blocks.size());
		Block block = blocks.get(0);
		assertArrayEquals(new byte[]{124}, block.block);
	}

	@Test
	public void testSplitBlocksFile() throws IOException {
		byte[] bytes = getFileBytes("/Test");
		ArrayList<Byte> fileBytes = new ArrayList<Byte>();
		for (byte b : bytes) {
			fileBytes.add(b);
		}
		
		List<Block> blocks = SplitBlocks.splitBlocks(bytes);
		ArrayList<Byte> recievedBytes = new ArrayList<Byte>();
		
		for (Block block : blocks) {
			assertTrue(block.block.length > 128);
			assertTrue(block.block.length < 4098);
			assertEquals(DigestUtils.md5Hex(block.block), block.md5Digest);
			for (byte b : block.block) {
				recievedBytes.add(b);
			}
		}
		
		assertEquals(fileBytes, recievedBytes);
	}
	
	
	
	@Test
	public void testSplitBlocksZeroStart() {
		byte[] bytes = {0, 124};
		
		List<Block> blocks = SplitBlocks.splitBlocks(bytes);
		assertEquals(1, blocks.size());
		Block block = blocks.get(0);
		assertArrayEquals(new byte[]{0, 124}, block.block);
	}

	@Test
	public void testSplitBlocksZeroEnd() {
		byte[] bytes = {123, 0};
		
		List<Block> blocks = SplitBlocks.splitBlocks(bytes);
		assertEquals(1, blocks.size());
		Block block = blocks.get(0);
		assertArrayEquals(new byte[]{123, 0}, block.block);
	}

	
	public byte[] getFileBytes(String path) throws IOException {
        File file = new File(System.getProperty("user.dir") + path);
        InputStream input = new BufferedInputStream(new FileInputStream(file));
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		IOUtils.copy(input, byteStream);
		input.close();
		byteStream.close();
		return byteStream.toByteArray();
	}

}
