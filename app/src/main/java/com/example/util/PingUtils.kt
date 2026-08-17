package com.example.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object PingUtils {
    suspend fun measureNetworkPing(host: String = "8.8.8.8", port: Int = 53, timeoutMs: Int = 2000): Long {
        return withContext(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            var socket: Socket? = null
            try {
                socket = Socket()
                socket.connect(InetSocketAddress(host, port), timeoutMs)
                val latency = System.currentTimeMillis() - startTime
                latency
            } catch (e: IOException) {
                // Try fallback to Cloudflare DNS 1.1.1.1
                try {
                    val fallbackStart = System.currentTimeMillis()
                    val fallbackSocket = Socket()
                    fallbackSocket.connect(InetSocketAddress("1.1.1.1", 53), timeoutMs)
                    fallbackSocket.close()
                    System.currentTimeMillis() - fallbackStart
                } catch (e2: Exception) {
                    -1L // Offline or timed out
                }
            } finally {
                try {
                    socket?.close()
                } catch (_: Exception) {}
            }
        }
    }
}
