import UIKit
import SwiftUI
import shared

// Хост для KMP/Compose
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        Main_iosKt.MainViewController()
    }
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    // Состояние для вывода результата
    @State private var lastMessage: String? = nil
    // Один экземпляр оценщика позы (наш Swift-обёртчик)
    private let estimator = IOSPoseEstimator()

    var body: some View {
        ZStack {
            // Твой Compose-контент, как и был
            ComposeView()
                .ignoresSafeArea(.all, edges: .bottom)

            // Плавающая кнопка "Снимок (iOS)" поверх Compose
            VStack {
                Spacer()
                HStack {
                    Spacer()
                    Button(action: onCapture) {
                        Text("Снимок (iOS)")
                            .font(.headline)
                            .padding(.horizontal, 16)
                            .padding(.vertical, 12)
                            .background(.ultraThinMaterial)
                            .clipShape(Capsule())
                    }
                    .padding(.trailing, 20)
                    .padding(.bottom, 28)
                }
            }
        }
        .alert(item: Binding.constant(lastMessage.map { MessageBox(text: $0) })) { msg in
            Alert(
                title: Text("Результат MediaPipe"),
                message: Text(msg.text),
                dismissButton: .default(Text("OK"))
            )
        }
    }

    private func onCapture() {
        guard let img = UIScreen.snapshotImage() else {
            lastMessage = "Не удалось получить изображение экрана"
            return
        }
        let result = estimator.estimate(image: img)
        lastMessage = result
    }
}

private struct MessageBox: Identifiable {
    let id = UUID()
    let text: String
}