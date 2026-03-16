package com.evenly.faceup.data.remote

/**
 * Supabase Configuration
 * 
 * Replace these values with your actual Supabase project credentials.
 * You can find these in your Supabase project settings:
 * Project Settings -> API -> URL and anon/public key
 */
object SupabaseConfig {
    // TODO: Replace with your Supabase URL
    const val SUPABASE_URL = "https://your-project-id.supabase.co"
    
    // TODO: Replace with your Supabase Anon Key
    const val SUPABASE_ANON_KEY = "your-anon-key-here"
    
    // API Endpoints
    const val AUTH_ENDPOINT = "/auth/v1"
    const val REST_ENDPOINT = "/rest/v1"
    const val STORAGE_ENDPOINT = "/storage/v1"
    const val REALTIME_ENDPOINT = "/realtime/v1"
}
