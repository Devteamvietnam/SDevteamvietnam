package com.devteamvietnam.common.utils.uuid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.devteamvietnam.common.exception.UtilException;

/**
 * Provide universally unique identifier (UUID) implementation
 *
 * @author Ivan
 */
public final class UUID implements java.io.Serializable, Comparable<UUID>
{
    private static final long serialVersionUID = -1185015143654744140L;

    /**
     * Singleton of SecureRandom
     *
     */
    private static class Holder
    {
        static final SecureRandom numberGenerator = getSecureRandom();
    }

    /** The most significant 64 bits of this UUID */
    private final long mostSigBits;

    /** The least significant 64 bits of this UUID */
    private final long leastSigBits;

    /**
     * Private structure
     *
     * @param data data
     */
    private UUID(byte[] data)
    {
        long msb = 0;
        long lsb = 0;
        assert data.length == 16: "data must be 16 bytes in length";
        for (int i = 0; i <8; i++)
        {
            msb = (msb << 8) | (data[i] & 0xff);
        }
        for (int i = 8; i <16; i++)
        {
            lsb = (lsb << 8) | (data[i] & 0xff);
        }
        this.mostSigBits = msb;
        this.leastSigBits = lsb;
    }
    /**
     * Use the specified data to construct a new UUID.
     *
     * @param mostSigBits is used for the most significant 64 bits of {@code UUID}
     * @param leastSigBits is used for the least significant 64 bits of {@code UUID}
     */
    public UUID(long mostSigBits, long leastSigBits)
    {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
    }

    /**
     * Get the static factory of type 4 (pseudo-randomly generated) UUID. Use an encrypted native thread pseudo-random number generator to generate the UUID.
     *
     * @return randomly generated {@code UUID}
     */
    public static UUID fastUUID()
    {
        return randomUUID(false);
    }

    /**
     * Get the static factory of type 4 (pseudo-randomly generated) UUID. Use an encrypted strong pseudo-random number generator to generate the UUID.
     *
     * @return randomly generated {@code UUID}
     */
    public static UUID randomUUID()
    {
        return randomUUID(true);
    }

    /**
     * Get the static factory of type 4 (pseudo-randomly generated) UUID. Use an encrypted strong pseudo-random number generator to generate the UUID.
     *
     * @param isSecure whether to use {@link SecureRandom} if it is to get a more secure random code, otherwise it can get better performance
     * @return randomly generated {@code UUID}
     */
    public static UUID randomUUID(boolean isSecure)
    {
        final Random ng = isSecure? Holder.numberGenerator: getRandom();

        byte[] randomBytes = new byte[16];
        ng.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f; /* clear version */
        randomBytes[6] |= 0x40; /* set to version 4 */
        randomBytes[8] &= 0x3f; /* clear variant */
        randomBytes[8] |= 0x80; /* set to IETF variant */
        return new UUID(randomBytes);
    }

    /**
     * Obtain a static factory of type 3 (name-based) UUID according to the specified byte array.
     *
     * @param name is used to construct a byte array of UUID.
     *
     * @return {@code UUID} generated according to the specified array
     */
    public static UUID nameUUIDFromBytes(byte[] name)
    {
        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException nsae)
        {
            throw new InternalError("MD5 not supported");
        }
        byte[] md5Bytes = md.digest(name);
        md5Bytes[6] &= 0x0f; /* clear version */
        md5Bytes[6] |= 0x30; /* set to version 3 */
        md5Bytes[8] &= 0x3f; /* clear variant */
        md5Bytes[8] |= 0x80; /* set to IETF variant */
        return new UUID(md5Bytes);
    }

    /**
     * Create {@code UUID} based on the standard representation of the string described in the {@link #toString()} method.
     *
     * @param name specifies {@code UUID} string
     * @return {@code UUID} with the specified value
     * @throws IllegalArgumentException This exception is thrown if the name does not match the string representation described in {@link #toString}
     *
     */
    public static UUID fromString(String name)
    {
        String[] components = name.split("-");
        if (components.length != 5)
        {
            throw new IllegalArgumentException("Invalid UUID string: "+ name);
        }
        for (int i = 0; i <5; i++)
        {
            components[i] = "0x" + components[i];
        }

        long mostSigBits = Long.decode(components[0]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[1]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[2]).longValue();

        long leastSigBits = Long.decode(components[3]).longValue();
        leastSigBits <<= 48;
        leastSigBits |= Long.decode(components[4]).longValue();

        return new UUID(mostSigBits, leastSigBits);
    }

    /**
     * Returns the least significant 64 bits of the 128-bit value of this UUID.
     *
     * @return The least significant 64 bits of the 128-bit value of this UUID.
     */
    public long getLeastSignificantBits()
    {
        return leastSigBits;
    }

    /**
     * Returns the most significant 64 bits of the 128-bit value of this UUID.
     *
     * @return The most significant 64 bits of the 128-bit value of this UUID.
     */
    public long getMostSignificantBits()
    {
        return mostSigBits;
    }

    /**
     * The version number associated with this {@code UUID}. The version number describes how this {@code UUID} was generated.
     * <p>
     * The version number has the following meaning:
     * <ul>
     * <li>1 Time-based UUID
     * <li>2 DCE security UUID
     * <li>3 UUID based on name
     * <li>4 Randomly generated UUID
     * </ul>
     *
     * @return the version number of this {@code UUID}
     */
    public int version()
    {
        // Version is bits masked by 0x000000000000F000 in MS long
        return (int) ((mostSigBits >> 12) & 0x0f);
    }

    /**
     * The variant number associated with this {@code UUID}. The variant number describes the layout of {@code UUID}.
     * <p>
     * The variant number has the following meaning:
     * <ul>
     * <li>0 is reserved for NCS backward compatibility
     * <li>2 <a href="http://www.ietf.org/rfc/rfc4122.txt">IETF&nbsp;RFC&nbsp;4122</a>(Leach-Salz), for this category
     * <li>6 Reserved, Microsoft is backward compatible
     * <li>7 Reserved for future definition
     * </ul>
     *
     * @return the variant number associated with this {@code UUID}
     */
    public int variant()
    {
        // This field is composed of a varying number of bits.
        // 0--Reserved for NCS backward compatibility
        // 1 0-The IETF aka Leach-Salz variant (used by this class)
        // 1 1 0 Reserved, Microsoft backward compatibility
        // 1 1 1 Reserved for future definition.
        return (int) ((leastSigBits >>> (64-(leastSigBits >>> 62))) & (leastSigBits >> 63));
    }
    
    /**
     * The timestamp value associated with this UUID.
     *
     * <p>
     * The 60-bit timestamp value is constructed according to the time_low, time_mid and time_hi fields of this {@code UUID}. <br>
     * The time stamp obtained is in units of 100 nanoseconds, starting from UTC (Universal Coordinated Time) at 0:00 on October 15, 1582.
     *
     * <p>
     * The timestamp value is only meaningful in the time-based UUID (its version type is 1). <br>
     * If this {@code UUID} is not a time-based UUID, this method throws an UnsupportedOperationException.
     *
     * @throws UnsupportedOperationException if this {@code UUID} is not a UUID with version 1.
     */
    public long timestamp() throws UnsupportedOperationException
    {
        checkTimeBase();
        return (mostSigBits & 0x0FFFL) << 48//
                | ((mostSigBits >> 16) & 0x0FFFFL) << 32//
                | mostSigBits >>> 32;
    }

    /**
     * The clock sequence value associated with this UUID.
     *
     * <p>
     * The 14-bit clock sequence value is constructed according to the clock_seq field of this UUID. The clock_seq field is used to ensure time uniqueness in the time-based UUID.
     * <p>
     * The {@code clockSequence} value is only meaningful in a time-based UUID (its version type is 1). If this UUID is not a time-based UUID, this method throws
     * UnsupportedOperationException.
     *
     * @return this {@code UUID} clock sequence
     *
     * @throws UnsupportedOperationException if the version of this UUID is not 1
     */
    public int clockSequence() throws UnsupportedOperationException
    {
        checkTimeBase();
        return (int) ((leastSigBits & 0x3FFF000000000000L) >>> 48);
    }

    /**
     * The node value associated with this UUID.
     *
     * <p>
     * The 48-bit node value is constructed according to the node field of this UUID. This field is used to save the IEEE 802 address of the machine, which is used to generate this UUID to ensure uniqueness in space.
     * <p>
     * The node value is only meaningful in the time-based UUID (its version type is 1). <br>
     * If this UUID is not a time-based UUID, this method throws an UnsupportedOperationException.
     *
     * @return the node value of this {@code UUID}
     *
     * @throws UnsupportedOperationException if the version of this UUID is not 1
     */
    public long node() throws UnsupportedOperationException
    {
        checkTimeBase();
        return leastSigBits & 0x0000FFFFFFFFFFFFL;
    }

    /**
     * Return the string representation of this {@code UUID}.
     *
     * <p>
     * The string representation of UUID is described by this BNF:
     *
     * <pre>
     * {@code
     * UUID = <time_low>-<time_mid>-<time_high_and_version>-<variant_and_sequence>-<node>
     * time_low = 4*<hexOctet>
     * time_mid = 2*<hexOctet>
     * time_high_and_version = 2*<hexOctet>
     * variant_and_sequence = 2*<hexOctet>
     * node = 6*<hexOctet>
     * hexOctet = <hexDigit><hexDigit>
     * hexDigit = [0-9a-fA-F]
     *}
     * </pre>
     *
     * </blockquote>
     *
     * @return the string representation of this {@code UUID}
     * @see #toString(boolean)
     */
    @Override
    public String toString()
    {
        return toString(false);
    }

    /**
     * Return the string representation of this {@code UUID}.
     *
     * <p>
     * The string representation of UUID is described by this BNF:
     *
     * <pre>
     * {@code
     * UUID = <time_low>-<time_mid>-<time_high_and_version>-<variant_and_sequence>-<node>
     * time_low = 4*<hexOctet>
     * time_mid = 2*<hexOctet>
     * time_high_and_version = 2*<hexOctet>
     * variant_and_sequence = 2*<hexOctet>
     * node = 6*<hexOctet>
     * hexOctet = <hexDigit><hexDigit>
     * hexDigit = [0-9a-fA-F]
     *}
     * </pre>
     *
     * </blockquote>
     *
     * @param isSimple is simple mode, simple mode is UUID string without'-'
     * @return the string representation of this {@code UUID}
     */
    public String toString(boolean isSimple)
    {
        final StringBuilder builder = new StringBuilder(isSimple? 32: 36);
        // time_low
        builder.append(digits(mostSigBits >> 32, 8));
        if (false == isSimple)
        {
            builder.append('-');
        }
        // time_mid
        builder.append(digits(mostSigBits >> 16, 4));
        if (false == isSimple)
        {
            builder.append('-');
        }
        // time_high_and_version
        builder.append(digits(mostSigBits, 4));
        if (false == isSimple)
        {
            builder.append('-');
        }
        // variant_and_sequence
        builder.append(digits(leastSigBits >> 48, 4));
        if (false == isSimple)
        {
            builder.append('-');
        }
        // node
        builder.append(digits(leastSigBits, 12));

        return builder.toString();
    }

    /**
     * Returns the hash code of this UUID.
     *
     * @return UUID hash code value.
     */
    @Override
    public int hashCode()
    {
        long hilo = mostSigBits ^ leastSigBits;
        return ((int) (hilo >> 32)) ^ (int) hilo;
    }

    /**
     * Compare this object with the specified object.
     * <p>
     * The result is {@code true} if and only if the parameter is not {@code null}, but a UUID object, has the same varriant as this UUID, and contains the same value (each bit is the same).
     *
     * @param obj the object to be compared
     *
     * @return If the objects are the same, return {@code true}; otherwise, return {@code false}
     */
    @Override
    public boolean equals(Object obj)
    {
        if ((null == obj) || (obj.getClass() != UUID.class))
        {
            return false;
        }
        UUID id = (UUID) obj;
        return (mostSigBits == id.mostSigBits && leastSigBits == id.leastSigBits);
    }
    
 // Comparison Operations

    /**
     * Compare this UUID with the specified UUID.
     *
     * <p>
     * If the two UUIDs are different, and the most significant field of the first UUID is greater than the corresponding field of the second UUID, the first UUID is greater than the second UUID.
     *
     * @param val UUID compared with this UUID
     *
     * @return When this UUID is less than, equal to or greater than val, return -1, 0 or 1, respectively.
     *
     */
    @Override
    public int compareTo(UUID val)
    {
        // The ordering is intentionally set up so that the UUIDs
        // can simply be numerically compared as two numbers
        return (this.mostSigBits <val.mostSigBits? -1: //
                (this.mostSigBits> val.mostSigBits? 1: //
                        (this.leastSigBits <val.leastSigBits? -1: //
                                (this.leastSigBits> val.leastSigBits? 1: //
                                        0))));
    }

    // ------------------------------------------------ -------------------------------------------------- -----------------
    // Private method start
    /**
     * Return the hex value corresponding to the specified number
     *
     * @param val value
     * @param digits
     * @return value
     */
    private static String digits(long val, int digits)
    {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi-1))).substring(1);
    }

    /**
     * Check whether it is a time-based version UUID
     */
    private void checkTimeBase()
    {
        if (version() != 1)
        {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }
    }

    /**
     * Get {@link SecureRandom}, a strong random number generator (RNG) that provides encryption
     *
     * @return {@link SecureRandom}
     */
    public static SecureRandom getSecureRandom()
    {
        try
        {
            return SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new UtilException(e);
        }
    }

    /**
     * Get random number generator object<br>
     * ThreadLocalRandom is to provide concurrent random number generation after JDK 7, which can solve the contention and contention of multiple threads.
     *
     * @return {@link ThreadLocalRandom}
     */
    public static ThreadLocalRandom getRandom()
    {
        return ThreadLocalRandom.current();
    }
}
