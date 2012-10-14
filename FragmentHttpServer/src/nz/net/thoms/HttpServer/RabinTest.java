package nz.net.thoms.HttpServer;

import static org.junit.Assert.*;

import org.junit.Test;

public class RabinTest {

	@Test
	public void testCalculateRabinStart() {
		byte[] bytes = {10};
		int result = Rabin.calculateRabin(bytes, 0, -1);
		assertEquals(10, result);
	}

	@Test
	public void testCalculateRabinSecondByte() {
		byte[] bytes = {10, 67};
		int result = Rabin.calculateRabin(bytes, 10, 0);
		assertEquals(1197, result);
	}
	
	@Test
	public void testCalculateRabinThirdByte() {
		byte[] bytes = {10, 67, 45};
		int result = Rabin.calculateRabin(bytes, 1197, 1);
		assertEquals(135306, result);
	}

	@Test
	public void testCalculateRabinFourthByte() {
		byte[] bytes = {10, 67, 45,123};
		int result = Rabin.calculateRabin(bytes, 135306, 2);
		assertEquals(860731, result);
	}

	@Test
	public void testCalculateRabinFifthByte() {
		byte[] bytes = {10, 67, 45,123, 5};
		int result = Rabin.calculateRabin(bytes, 860731, 3);
		assertEquals(588509, result);
	}

	@Test
	public void testCalculateRabinSixthByte() {
		byte[] bytes = {10, 67, 45,123, 5, 100};
		int result = Rabin.calculateRabin(bytes, 588509, 4);
		assertEquals(1571252, result);
	}
}

// 10 * 