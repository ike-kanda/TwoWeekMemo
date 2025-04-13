'use client'
import { useEffect, useState } from 'react'
import { useParams, useRouter } from 'next/navigation'
import { fetchWithAuth } from '../../lib/api'

export default function MemoEditPage() {
  const { id } = useParams()
  const router = useRouter()
  const [content, setContent] = useState('')

  useEffect(() => {
    const fetchMemo = async () => {
      try {
        const res = await fetchWithAuth(`/api/memos`)
        const data = await res.json()
        const memo = data.find((m: any) => m.id === Number(id))
        if (memo) setContent(memo.content)
      } catch (err) {
        console.error('メモ取得失敗:', err)
      }
    }

    fetchMemo()
  }, [id])

  useEffect(() => {
    const timeout = setTimeout(() => {
      fetchWithAuth(`/api/memos/${id}`, {
        method: 'PUT',
        body: JSON.stringify({ content }),
        headers: { 'Content-Type': 'application/json' },
      }).catch((err) => console.error('自動保存失敗:', err))
    }, 1000)
    return () => clearTimeout(timeout)
  }, [content, id])

  const handleDelete = async () => {
    try {
      await fetchWithAuth(`/api/memos/${id}`, { method: 'DELETE' })
      router.push('/memos')
    } catch (err) {
      console.error('削除失敗:', err)
    }
  }

  return (
    <div className="space-y-4">
      <button
        onClick={() => router.push('/memos')}
        className="text-sm text-blue-500 hover:underline"
      >
        ← メモ一覧に戻る
      </button>

      <div className="flex justify-between items-center mb-2">
        <h1 className="text-xl font-bold">メモ編集</h1>
        <button
          onClick={handleDelete}
          className="text-red-500 hover:text-red-700"
        >削除</button>
      </div>

      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
      />
      <div className="text-sm text-gray-400 mt-1">自動保存されています</div>
    </div>
  )
} 
