package com.moac.android.interceptordemo.api;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class ArtworkUrlConverterTest {

    private ArtworkUrlConverter mArtworkUrlConverter;

    @Before
    public void setUp() {
        mArtworkUrlConverter = new ArtworkUrlConverter();
    }

    @Test
    public void testConvertToHighResUrl_returnsNull_whenEmpty() {
        assertThat(mArtworkUrlConverter.convertToHighResUrl("")).isNull();
    }

    @Test
    public void testConvertToHighResUrl_returnsNull_whenNull() {
        assertThat(mArtworkUrlConverter.convertToHighResUrl(null)).isNull();
    }

    @Test
    public void testThing_returnsNull_whenDoesNotContainLargeToken() {
        assertThat(mArtworkUrlConverter.convertToHighResUrl("https://www.dummy.com/dummyImage.jpg"))
                .isNull();
    }

    @Test
    public void testThing_returnsReplacedLargeToken_whenContainsLargeToken() {
        assertThat(mArtworkUrlConverter
                           .convertToHighResUrl("https://www.dummy.com/dummyImage-large.jpg"))
                .isEqualTo("https://www.dummy.com/dummyImage-t500x500.jpg");
    }

    @Test
    public void testThing_returnsReplacedLargeToken_whenContainsDuplicateLargeToken() {
        assertThat(mArtworkUrlConverter
                           .convertToHighResUrl(
                                   "https://www.dummy.com/dummy-large-Image-large.jpg"))
                .isEqualTo("https://www.dummy.com/dummy-large-Image-t500x500.jpg");
    }

}