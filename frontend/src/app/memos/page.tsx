'use client'
import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import { fetchWithAuth } from '../lib/api'

interface Memo {
  id: number
  content: string
}

export default function MemosPage() {
  const [memos, setMemos] = useState<Memo[]>([])
  const router = useRouter()

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetchWithAuth('/api/memos')
        const data = await res.json()
        console.log('取得したメモ:', data)
        setMemos(data)
      } catch (err) {
        console.error('メモの取得に失敗:', err)
      }
    }

    fetchData()
  }, [])

  const handleDelete = async (id: number) => {
    try {
      await fetchWithAuth(`/api/memos/${id}`, { method: 'DELETE' })
      setMemos((prev) => prev.filter((m) => m.id !== id))
    } catch (err) {
      console.error('削除に失敗:', err)
    }
  }

  const handleCreate = async () => {
    try {
      const res = await fetchWithAuth('/api/memos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ content: '新規メモ' }),
      })
      const newMemo = await res.json()
      router.push(`/memos/${newMemo.id}`)
    } catch (err) {
      console.error('新規作成に失敗:', err)
    }
  }

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">メモ一覧</h1>
        <button
          onClick={handleCreate}
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          ＋ 新規作成
        </button>
      </div>

      {memos.map((memo) => (
        <div
          key={memo.id}
          className="flex justify-between items-center border-b py-2 cursor-pointer hover:bg-gray-100 px-2 rounded"
          onClick={() => router.push(`/memos/${memo.id}`)}
        >
          <div className="text-gray-700">{memo.content.slice(0, 20)}...</div>
          <button
            className="text-red-500 hover:text-red-700 ml-2"
            onClick={(e) => {
              e.stopPropagation()
              handleDelete(memo.id)
            }}
          >削除</button>
        </div>
      ))}
    </div>
  )
} 