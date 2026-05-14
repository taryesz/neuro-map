import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    handleIncomingURL(url: url)
                }
        }
    }
    
    func handleIncomingURL(url: URL) {
    
        guard url.scheme == "neuromapa", url.host == "auth" else { return }
        
        var params: [String: String] = [:]
        
        if let fragment = url.fragment {
            let pairs = fragment.split(separator: "&")
            for pair in pairs {
                let kv = pair.split(separator: "=", maxSplits: 1)
                if kv.count == 2 {
                    params[String(kv[0])] = String(kv[1])
                }
            }
        }

        let accessToken = params["access_token"]
        let refreshToken = params["refresh_token"]
        let errorDesc = params["error_description"] ?? params["error"]

        OAuthResultHandler.shared.handle?(accessToken, refreshToken, errorDesc)
    }
}
