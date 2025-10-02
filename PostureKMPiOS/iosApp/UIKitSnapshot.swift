import UIKit

extension UIWindow {
    static var activeKeyWindow: UIWindow? {
        return UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .first { $0.isKeyWindow }
    }
}

extension UIScreen {
    static func snapshotImage() -> UIImage? {
        guard let window = UIWindow.activeKeyWindow else { return nil }
        let renderer = UIGraphicsImageRenderer(bounds: window.bounds)
        return renderer.image { ctx in
            window.layer.render(in: ctx.cgContext)
        }
    }
}