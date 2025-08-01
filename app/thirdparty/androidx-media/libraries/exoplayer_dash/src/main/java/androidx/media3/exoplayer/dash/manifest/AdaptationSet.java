/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.dash.manifest;

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import java.util.Collections;
import java.util.List;

/** Represents a set of interchangeable encoded versions of a media content component. */
@UnstableApi
public class AdaptationSet {

  /** Value of {@link #id} indicating no value is set.= */
  public static final long ID_UNSET = -1;

  /**
   * A non-negative identifier for the adaptation set that's unique in the scope of its containing
   * period, or {@link #ID_UNSET} if not specified.
   */
  public final long id;

  /** The {@link C.TrackType track type} of the adaptation set. */
  public final @C.TrackType int type;

  /** {@link Representation}s in the adaptation set. */
  public final List<Representation> representations;

  /** Accessibility descriptors in the adaptation set. */
  public final List<Descriptor> accessibilityDescriptors;

  /** Essential properties in the adaptation set. */
  public final List<Descriptor> essentialProperties;

  /** Supplemental properties in the adaptation set. */
  public final List<Descriptor> supplementalProperties;

  /**
   * @param id A non-negative identifier for the adaptation set that's unique in the scope of its
   *     containing period, or {@link #ID_UNSET} if not specified.
   * @param type The {@link C.TrackType track type} of the adaptation set.
   * @param representations {@link Representation}s in the adaptation set.
   * @param accessibilityDescriptors Accessibility descriptors in the adaptation set.
   * @param essentialProperties Essential properties in the adaptation set.
   * @param supplementalProperties Supplemental properties in the adaptation set.
   */
  public AdaptationSet(
      long id,
      @C.TrackType int type,
      List<Representation> representations,
      List<Descriptor> accessibilityDescriptors,
      List<Descriptor> essentialProperties,
      List<Descriptor> supplementalProperties) {
    this.id = id;
    this.type = type;
    this.representations = Collections.unmodifiableList(representations);
    this.accessibilityDescriptors = Collections.unmodifiableList(accessibilityDescriptors);
    this.essentialProperties = Collections.unmodifiableList(essentialProperties);
    this.supplementalProperties = Collections.unmodifiableList(supplementalProperties);
  }
}
