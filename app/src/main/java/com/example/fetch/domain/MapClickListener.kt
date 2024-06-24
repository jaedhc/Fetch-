package com.example.fetch.domain

import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint

class MapClickListener: MapEventsReceiver {
    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        if (p != null) {
            val latitude = p.latitude
            val longitude = p.longitude
            // Use the latitude and longitude as needed (e.g., display them, add a marker, etc.)
        }
        return true // Return true to consume the event
    }

    override fun longPressHelper(p: GeoPoint?): Boolean {
        return false
    }

}