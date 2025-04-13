import './globals.css'
import { ReactNode } from 'react'

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="ja">
      <body>
        <main className="max-w-2xl mx-auto py-10 px-4">{children}</main>
      </body>
    </html>
  )
}